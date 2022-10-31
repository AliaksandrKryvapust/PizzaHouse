package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MenuDao implements IMenuDao {
    private static MenuDao firstInstance = null;
    private final DataSource dataSource;
    private static final String SELECT_MENU_BY_ID_SQL = "SELECT id, created_at, version, name, enabled " +
            "FROM pizza_manager.menu WHERE id=?;";
    private static final String SELECT_MENU_SQL = "SELECT id, created_at, version, name, enabled " +
            "FROM pizza_manager.menu ORDER BY id;";

    public MenuDao() {

        try {
            this.dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class at MenuDao", e);
        }
    }

    public static MenuDao getInstance() {
        synchronized (MenuDao.class) {
            if (firstInstance == null) {
                firstInstance = new MenuDao();
            }
        }
        return firstInstance;
    }

    @Override
    public List<IMenu> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenu> menus = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Menu menu = new Menu();
                        menu.setId(resultSet.getLong("id"));
                        menu.setCreationDate(resultSet.getTimestamp("created_at").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("version"));
                        menu.setName(resultSet.getString("name"));
                        menu.setEnable(resultSet.getBoolean("enabled"));
                        menus.add(menu);
                    }
                }
            }
            return menus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IMenu get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            Menu menu = new Menu();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menu.setId(resultSet.getLong("id"));
                        menu.setCreationDate(resultSet.getTimestamp("created_at").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("version"));
                        menu.setName(resultSet.getString("name"));
                        menu.setEnable(resultSet.getBoolean("enabled"));
                    }
                    return menu;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(IMenu imenu) {
        Menu menu = (Menu) imenu;
        if (menu.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?);";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                for (MenuItem item : menu.getItems()) {
                    statement.setString(1, item.getInfo().getName());
                    statement.setString(2, item.getInfo().getDescription());
                    statement.setLong(3, item.getInfo().getSize());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    for (MenuItem item : menu.getItems()) {
                        generatedKeys.next();
                        item.getInfo().setId(generatedKeys.getLong(1));
                    }
                }
                if (rows == null) {
                    throw new SQLException("pizza_info table insert failed, no rows affected");
                }
            }
            String menuSql = "INSERT INTO pizza_manager.menu (name, enabled)\n VALUES (?, ?)";
            try (PreparedStatement statement = con.prepareStatement(menuSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                    statement.setString(1, menu.getName());
                    statement.setBoolean(2, menu.getEnable());
                    rows += statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    while (generatedKeys.next()) {
                        menu.setId(generatedKeys.getLong(1));
                    }
                }
                if (rows == 0) {
                    throw new SQLException("menu table insert failed, no rows affected");
                }
            }
            String menuItemSql = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id, menu_id)\n VALUES (?, ?, ?);";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql, Statement.RETURN_GENERATED_KEYS)) {
                for (MenuItem item : menu.getItems()) {
                    statement.setDouble(1, item.getPrice());
                    statement.setLong(2, item.getInfo().getId());
                    statement.setLong(3,menu.getId());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    for (MenuItem item : menu.getItems()) {
                        generatedKeys.next();
                        item.setId(generatedKeys.getLong(1));
                    }
                }
                if (rows == null) {
                    throw new SQLException("menu_item table insert failed, no rows affected");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IMenu IMenu) {
        Menu menu = (Menu) IMenu;
        if (!this.exist(menu.getId())) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "SELECT pizza_info.version, pizza_info.id\n FROM pizza_manager.pizza_info\n" +
                    "INNER JOIN pizza_manager.menu_item ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "INNER JOIN pizza_manager.menu ON menu.id=menu_item.menu_id\n" +
                    "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n " +
                    "ORDER BY pizza_info.id;";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                statement.setLong(1, menu.getId());
                statement.setInt(2, menu.getVersion());
                try (ResultSet resultSet = statement.executeQuery()) {
                        for (MenuItem item : menu.getItems()) {
                            resultSet.next();
                            item.getInfo().setVersion(resultSet.getInt("version"));
                            item.getInfo().setId(resultSet.getLong("id"));
                        }
                    }
                }
                String menuItemSql = "SELECT menu_item.version, menu_item.id\n FROM pizza_manager.menu_item\n" +
                        "INNER JOIN pizza_manager.menu ON menu.id=menu_item.menu_id\n" +
                        "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n " +
                        "ORDER BY menu_item.id;";
                try (PreparedStatement statement = con.prepareStatement(menuItemSql)) {
                    statement.setLong(1, menu.getId());
                    statement.setInt(2, menu.getVersion());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        for (MenuItem item : menu.getItems()) {
                            resultSet.next();
                            item.setVersion(resultSet.getInt("version"));
                            item.setId(resultSet.getLong("id"));
                        }
                    }
                }
                String menuSql = "UPDATE pizza_manager.menu\n SET version=version+1, name=?, enabled=?\n " +
                        "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(menuSql)) {
                long rows = 0;
                statement.setString(1, menu.getName());
                statement.setBoolean(2, menu.getEnable());
                statement.setLong(3, menu.getId());
                statement.setInt(4, menu.getVersion());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table update failed, no rows affected");
                }
            }
            String menuItemSqlUpdate = "UPDATE pizza_manager.menu_item\n " +
                    "SET price=?, version=version+1, menu_id=?\n" +
                    "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(menuItemSqlUpdate)) {
                for (MenuItem items : menu.getItems()) {
                    statement.setDouble(1, items.getPrice());
                    statement.setLong(2, menu.getId());
                    statement.setLong(3, items.getId());
                    statement.setInt(4, items.getVersion());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                if (rows == null) {
                    throw new SQLException("menu_item table update failed, no rows affected");
                }
            }
            String pizzaInfoSqlUpdate = "UPDATE pizza_manager.pizza_info\n " +
                    "SET name=?, description=?, size=?, version=version+1\n" +
                    "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlUpdate)) {
                for (MenuItem items : menu.getItems()) {
                    statement.setString(1, items.getInfo().getName());
                    statement.setString(2, items.getInfo().getDescription());
                    statement.setLong(3, items.getInfo().getSize());
                    statement.setLong(4, items.getInfo().getId());
                    statement.setInt(5, items.getInfo().getVersion());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                if (rows == null) {
                    throw new SQLException("pizza_info table update failed, no rows affected");
                }
            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void add(MenuItem menuItem, Long menuId) {
        if (!this.exist(menuId)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setString(1, menuItem.getInfo().getName());
                statement.setString(2, menuItem.getInfo().getDescription());
                statement.setLong(3, menuItem.getInfo().getSize());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table update failed, no rows affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        menuItem.getInfo().setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("pizza_info table update failed, no generated id returned");
                    }
                }
            }
            String menuItemSql = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id, menu_id)\n VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setDouble(1, menuItem.getPrice());
                statement.setLong(2, menuItem.getInfo().getId());
                statement.setLong(3, menuId);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table update failed, no rows affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        menuItem.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("menu_item table update failed, no generated id returned");
                    }
                }
            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void delete(Long id, Integer version) {
        if (!this.exist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "SELECT pizza_info.version, pizza_info.id\n FROM pizza_manager.pizza_info\n" +
                    "INNER JOIN pizza_manager.menu_item ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "INNER JOIN pizza_manager.menu ON menu.id=menu_item.menu_id\n" +
                    "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n " +
                    "ORDER BY pizza_info.id;";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                statement.setLong(1, id);
                statement.setInt(2, version);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        PizzaInfo pizzaInfo = new PizzaInfo();
                        pizzaInfo.setVersion(resultSet.getInt("version"));
                        pizzaInfo.setId(resultSet.getLong("id"));
                        MenuItem menuItem = new MenuItem();
                        menuItem.setPizzaInfo(pizzaInfo);
                        menuItems.add(menuItem);
                    }
                }
            }
            String menuItemSql = "SELECT menu_item.version, menu_item.id\n FROM pizza_manager.menu_item\n" +
                    "INNER JOIN pizza_manager.menu ON menu.id=menu_item.menu_id\n" +
                    "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n " +
                    "ORDER BY menu_item.id;";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql)) {
                statement.setLong(1, id);
                statement.setInt(2, version);
                try (ResultSet resultSet = statement.executeQuery()) {
                    for (MenuItem item : menuItems) {
                        resultSet.next();
                        item.setVersion(resultSet.getInt("version"));
                        item.setId(resultSet.getLong("id"));
                    }
                }
            }
            String menuSql = "DELETE FROM pizza_manager.menu\n " +
                    "WHERE pizza_manager.menu.id=? AND pizza_manager.menu.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(menuSql)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table delete failed, no rows affected");
                }
            }
            String menuItemSqlDelete = "DELETE FROM pizza_manager.menu_item\n " +
                    "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(menuItemSqlDelete)) {
                for (MenuItem items : menuItems) {
                    statement.setLong(1, items.getId());
                    statement.setInt(2, items.getVersion());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                if (rows == null) {
                    throw new SQLException("menu_item table delete failed, no rows affected");
                }
            }
            String pizzaInfoSqlDelete = "DELETE FROM pizza_manager.pizza_info\n " +
                    "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlDelete)) {
                for (MenuItem items : menuItems) {
                    statement.setLong(1, items.getInfo().getId());
                    statement.setInt(2, items.getInfo().getVersion());
                    statement.addBatch();
                }
                int[] rows = statement.executeBatch();
                if (rows == null) {
                    throw new SQLException("pizza_info table delete failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id FROM pizza_manager.menu\n WHERE id=?\n ORDER BY id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean isDishExist(String name) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT name FROM pizza_manager.pizza_info\n WHERE name=?\n ORDER BY name;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
