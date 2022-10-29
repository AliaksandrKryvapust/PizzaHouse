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
            long id= 0;
            String sqlId = "SELECT COUNT(DISTINCT menu_id) AS id FROM pizza_manager.menu_item\n" +
                    "ORDER BY id";
            try (PreparedStatement statement = con.prepareStatement(sqlId)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        id = (resultSet.getLong("id"));
                    }
                }
            }
            List<IMenu> menus = new ArrayList<>();
            for (int i = 0; i < id; i++) {
                String sql = "SELECT COUNT(id) AS count FROM pizza_manager.menu_item\n" +
                        "WHERE menu_id=?\n GROUP BY menu_id\n ORDER BY count";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setLong(1, i + 1);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            List<MenuItem> items = new ArrayList<>();
                            for (int j = 0; j < resultSet.getLong("count"); j++) {
                                items.add(new MenuItem(new PizzaInfo()));
                            }
                            menus.add(new Menu(items, (long) (i + 1)));
                        } else {
                            throw new SQLException("SELECT COUNT(menu_item.id) failed, no data returned");
                        }
                    }
                }
                String sqlNew = "SELECT id FROM pizza_manager.menu_item\n" +
                        "WHERE menu_id=?\n ORDER BY id";
                try (PreparedStatement statement = con.prepareStatement(sqlNew)) {
                    statement.setLong(1, i + 1);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        for (MenuItem item : menus.get(i).getItems()) {
                            resultSet.next();
                            item.setId(resultSet.getLong("id"));
                        }
                    }
                }
            }
            String sql = "SELECT pizza_manager.menu.id, pizza_manager.menu.creation_date AS mcd, pizza_manager.menu.version AS med," +
                    "pizza_manager.menu.name AS mn, pizza_manager.menu.enabled AS me," +
                    "pizza_manager.menu_item.id,  pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version  AS mied, " +
                    "price, pizza_info_id, pizza_manager.pizza_info.name, description, size, pizza_manager.pizza_info.creation_date AS picd, " +
                    "pizza_manager.pizza_info.version  AS pied\n FROM pizza_manager.menu\n" +
                    "INNER JOIN pizza_manager.menu_item ON menu_item.menu_id=menu.id\n" +
                    "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "ORDER BY id, menu_item_id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        for (IMenu imenu : menus) {
                            Menu menu = (Menu) imenu;
                            if (menu.getId() == resultSet.getLong("id")) {
                                menu.setCreationDate(resultSet.getTimestamp("mcd").toLocalDateTime());
                                menu.setVersion(resultSet.getInt("med"));
                                menu.setName(resultSet.getString("mn"));
                                menu.setEnable(resultSet.getBoolean("me"));
                                for (MenuItem item : menu.getItems()) {
                                    if (item.getId() == resultSet.getLong("menu_item_id")) {
                                        double price = resultSet.getDouble("price");
                                        if (!resultSet.wasNull()) {
                                            item.setPrice(price);
                                        }
                                        item.setCreationDate(resultSet.getTimestamp("micd").toLocalDateTime());
                                        item.setVersion(resultSet.getInt("mied"));
                                        long pizza_info_id = resultSet.getLong("pizza_info_id");
                                        if (!resultSet.wasNull()) {
                                            item.getInfo().setId(pizza_info_id);
                                        }
                                        item.getInfo().setName(resultSet.getString("name"));
                                        item.getInfo().setDescription(resultSet.getString("description"));
                                        long size = resultSet.getLong("size");
                                        if (!resultSet.wasNull()) {
                                            item.getInfo().setSize(size);
                                        }
                                        item.getInfo().setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                                        item.getInfo().setVersion(resultSet.getInt("pied"));
                                    }
                                }
                            }
                        }
                    }
                    return menus;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    SELECT pizza_manager.menu.id, pizza_manager.menu.creation_date, pizza_manager.menu.edit_date,
//    menu_item_id,  pizza_manager.menu_item.creation_date, pizza_manager.menu_item.edit_date,
//    price, pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date, pizza_manager.pizza_info.edit_date
//    FROM pizza_manager.menu
//    INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id
//    INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id
//    ORDER BY id, menu_item_id, pizza_info_id;

    @Override
    public IMenu get(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            Menu menu = new Menu();
            menu.setId(id);
            List<MenuItem> list = new ArrayList<>();
            String sql = "SELECT pizza_manager.menu.id,pizza_manager.menu.creation_date AS mcd, pizza_manager.menu.version AS med, " +
                    "pizza_manager.menu.name AS mn, pizza_manager.menu.enabled AS me," +
                    "pizza_manager.menu_item.id, price, pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version AS mied," +
                    "pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date AS picd, pizza_manager.pizza_info.version AS pied\n " +
                    "FROM pizza_manager.menu\n INNER JOIN pizza_manager.menu_item ON menu.id=menu_item.menu_id\n" +
                    "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "WHERE pizza_manager.menu.id=?\n ORDER BY id, pizza_manager.menu_item.id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menu.setCreationDate(resultSet.getTimestamp("mcd").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("med"));
                        menu.setName(resultSet.getString("mn"));
                        menu.setEnable(resultSet.getBoolean("me"));
                        MenuItem item = new MenuItem(new PizzaInfo());
                        item.setId(resultSet.getLong("menu_item_id"));
                        double price = resultSet.getDouble("price");
                        if (!resultSet.wasNull()) {
                            item.setPrice(price);
                        }
                        item.setCreationDate(resultSet.getTimestamp("micd").toLocalDateTime());
                        item.setVersion(resultSet.getInt("mied"));
                        long itemId = resultSet.getLong("pizza_info_id");
                        if (!resultSet.wasNull()) {
                            item.getInfo().setId(itemId);
                        }
                        item.getInfo().setName(resultSet.getString("name"));
                        item.getInfo().setDescription(resultSet.getString("description"));
                        long size = resultSet.getLong("size");
                        if (!resultSet.wasNull()) {
                            item.getInfo().setSize(size);
                        }
                        item.getInfo().setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                        item.getInfo().setVersion(resultSet.getInt("pied"));
                        list.add(item);
                    }
                    menu.setItems(list);
                    return menu;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    SELECT pizza_manager.menu.id, menu_item_id, price, pizza_info_id, name, description, size
//    FROM pizza_manager.menu INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id
//    INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id
//    WHERE pizza_manager.menu.id=1 ORDER BY id, menu_item_id, pizza_info_id;

    //   "SELECT menu_item_id, price, pizza_info_id, name, description, size\n FROM pizza_manager.menu\n" +
//            "INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id\n" +
//            "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
//            "WHERE pizza_manager.menu.id=?\n ORDER BY menu_item_id, pizza_info_id;";
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
        if (!this.isIdExist(menu.getId())) {
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
        if (!this.isIdExist(menuId)) {
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
        if (!this.isIdExist(id)) {
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
    public Boolean isIdExist(Long id) {
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
