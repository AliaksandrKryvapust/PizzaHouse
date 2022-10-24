package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDao implements IMenuItemDao {
    private static MenuItemDao firstInstance = null;
    private final DataSource dataSource;
    public MenuItemDao() {
        try {
            this.dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class at MenuItemDao", e);
        }
    }
    public static MenuItemDao getInstance() {
        synchronized (MenuItemDao.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemDao();
            }
        }
        return firstInstance;
    }
    @Override
    public void save(IMenuItem iMenuItem){
        MenuItem menuItem = (MenuItem) iMenuItem;
        if (menuItem.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?);";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                    statement.setString(1, menuItem.getInfo().getName());
                    statement.setString(2, menuItem.getInfo().getDescription());
                    statement.setLong(3, menuItem.getInfo().getSize());
                    rows += statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        while (generatedKeys.next()) {
                            menuItem.getInfo().setId(generatedKeys.getLong(1));
                        }
                    }
                if (rows == 0) {
                    throw new SQLException("pizza_info table insert failed, no rows affected");
                }
            }
            String menuItemSql = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id)\n VALUES (?, ?);";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql)) {
                long rows = 0;
                    statement.setDouble(1, menuItem.getPrice());
                    statement.setLong(2, menuItem.getInfo().getId());
                    rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table insert failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IMenuItem iMenuItem) throws SQLException {
        MenuItem menuItem = (MenuItem) iMenuItem;
        if (this.isIdExist(menuItem.getId())) {
            try (Connection con = dataSource.getConnection()) {
                String pizzaInfoSql = "SELECT pizza_info.version, pizza_info.id\n FROM pizza_manager.pizza_info\n" +
                        "INNER JOIN pizza_manager.menu_item ON menu_item.pizza_info_id=pizza_info.id\n" +
                        "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n " +
                        "ORDER BY pizza_info.id;";
                try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                    statement.setLong(1, menuItem.getId());
                    statement.setInt(2, menuItem.getVersion());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            menuItem.getInfo().setVersion(resultSet.getInt("version"));
                            menuItem.getInfo().setId(resultSet.getLong("id"));
                        }
                    }
                }
                String menuItemSqlUpdate = "UPDATE pizza_manager.menu_item\n " +
                        "SET price=?, version=version+1\n" +
                        "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n";
                try (PreparedStatement statement = con.prepareStatement(menuItemSqlUpdate)) {
                    long rows = 0;
                    statement.setDouble(1, menuItem.getPrice());
                    statement.setLong(2, menuItem.getId());
                    statement.setInt(3, menuItem.getVersion());
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("menu_item table update failed, no rows affected");
                    }
                }
                String pizzaInfoSqlUpdate = "UPDATE pizza_manager.pizza_info\n " +
                        "SET name=?, description=?, size=?, version=version+1\n" +
                        "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
                try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlUpdate)) {
                    long rows = 0;
                    statement.setString(1, menuItem.getInfo().getName());
                    statement.setString(2, menuItem.getInfo().getDescription());
                    statement.setLong(3, menuItem.getInfo().getSize());
                    statement.setLong(4, menuItem.getInfo().getId());
                    statement.setInt(5, menuItem.getInfo().getVersion());
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("pizza_info table update failed, no rows affected");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalStateException("Error code 500. MenuItem id is not valid");
    }

    @Override
    public IMenuItem get(Long id) throws SQLException {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            MenuItem item = new MenuItem(new PizzaInfo());
            item.setId(id);
            String sql = "SELECT pizza_manager.menu_item.id AS miid, price, pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version AS mied," +
                    "pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date AS picd, pizza_manager.pizza_info.version AS pied\n " +
                    "FROM pizza_manager.menu_item\n" +
                    "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "WHERE pizza_manager.menu_item.id=?\n ORDER BY menu_item.id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        item.setId(resultSet.getLong("miid"));
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
                    }
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id, Integer version){
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. MenuItem id is not valid");
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.setVersion(version);
        menuItem.setPizzaInfo(new PizzaInfo());
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "SELECT pizza_info.version, pizza_info.id\n FROM pizza_manager.pizza_info\n" +
                    "INNER JOIN pizza_manager.menu_item ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n " +
                    "ORDER BY pizza_info.id;";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                statement.setLong(1, id);
                statement.setInt(2, version);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menuItem.getInfo().setVersion(resultSet.getInt("version"));
                        menuItem.getInfo().setId(resultSet.getLong("id"));
                    }
                }
            }
            String menuItemSqlDelete = "DELETE FROM pizza_manager.menu_item\n " +
                    "WHERE pizza_manager.menu_item.id=? AND pizza_manager.menu_item.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(menuItemSqlDelete)) {
                long rows = 0;
                statement.setLong(1, menuItem.getId());
                statement.setInt(2, menuItem.getVersion());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table delete failed, no rows affected");
                }
            }
            String pizzaInfoSqlDelete = "DELETE FROM pizza_manager.pizza_info\n " +
                    "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlDelete)) {
                long rows = 0;
                statement.setLong(1, menuItem.getInfo().getId());
                statement.setInt(2, menuItem.getInfo().getVersion());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table delete failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MenuItem> get() {
        try (Connection con = dataSource.getConnection()) {
            List<MenuItem> items = new ArrayList<>();
            String sql = "SELECT COUNT(menu_item.id) AS count FROM pizza_manager.menu_item\n" + "ORDER BY count";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        for (int j = 0; j < resultSet.getLong("count"); j++) {
                            items.add(new MenuItem(new PizzaInfo()));
                        }
                    } else {
                        throw new SQLException("SELECT COUNT(menu_item.id) failed, no data returned");
                    }
                }
            }
            String sqlNew = "SELECT menu_item.id FROM pizza_manager.menu_item\n" + "ORDER BY id";
            try (PreparedStatement statement = con.prepareStatement(sqlNew)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    for (MenuItem item : items) {
                        resultSet.next();
                        item.setId(resultSet.getLong("id"));
                    }
                }
            }
            String sqlSelect = "SELECT pizza_manager.menu_item.id AS miid,  pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version  AS mied, " +
                    "price, pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date AS picd, pizza_manager.pizza_info.version  AS pied\n" +
                    "FROM pizza_manager.menu_item\n" + "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "ORDER BY miid, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        for (MenuItem item : items) {
                            if (item.getId() == resultSet.getLong("miid")) {
                                double price = resultSet.getDouble("price");
                                if (!resultSet.wasNull()) {
                                    item.setPrice(price);
                                }
                                item.setCreationDate(resultSet.getTimestamp("micd").toLocalDateTime());
                                item.setVersion(resultSet.getInt("mied"));
                                long id = resultSet.getLong("pizza_info_id");
                                if (!resultSet.wasNull()) {
                                    item.getInfo().setId(id);
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
                return items;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Boolean isIdExist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id FROM pizza_manager.menu_item\n WHERE id=?\n ORDER BY id;";
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
}
