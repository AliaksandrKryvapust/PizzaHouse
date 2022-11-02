package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDao implements IMenuItemDao {
    private final DataSource dataSource;
    private static final String INSERT_MENU_ITEM_SQL = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id, menu_id)" +
            " VALUES (?, ?, ?);";
    private static final String UPDATE_MENU_ITEM_SQL = "UPDATE pizza_manager.menu_item SET price=?, pizza_info_id=?, " +
            "menu_id=?, version=version+1 WHERE id=? AND version=?;";
    private static final String SELECT_MENU_ITEM_BY_ID_SQL = "SELECT id, price, pizza_info_id, creation_date, version," +
            " menu_id FROM pizza_manager.menu_item WHERE id=?;";

    public MenuItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IMenuItem save(IMenuItem menuItem) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_MENU_ITEM_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setDouble(1, menuItem.getPrice());
                statement.setLong(2, menuItem.getPizzaInfoId());
                statement.setLong(3, menuItem.getMenuId());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu_item table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new MenuItem(generatedKeys.getLong(1), menuItem.getPrice(),
                            menuItem.getPizzaInfoId(), menuItem.getMenuId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new MenuItem");
        }
    }

    @Override
    public IMenuItem update(IMenuItem menuItem, Long id, Integer version) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(UPDATE_MENU_ITEM_SQL)) {
                long rows = 0;
                statement.setDouble(1, menuItem.getPrice());
                statement.setLong(2, menuItem.getPizzaInfoId());
                statement.setLong(3, menuItem.getMenuId());
                statement.setLong(4, id);
                statement.setInt(5, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new IllegalArgumentException("menu_item table update failed, version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu_item table update, more than 1 row affected");
                }
                return new MenuItem(id, menuItem.getPrice(), menuItem.getPizzaInfoId(), menuItem.getMenuId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update menu_item by id:" + id);
        }
    }

    @Override
    public IMenuItem get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_ITEM_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Menu Item by id:" + id);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete){
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
    public List<IMenuItem> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenuItem> items = new ArrayList<>();
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
                    for (IMenuItem iMenuItem : items) {
                        MenuItem menuItem = (MenuItem) iMenuItem;
                        resultSet.next();
                        menuItem.setId(resultSet.getLong("id"));
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
                        for (IMenuItem iMenuItem : items) {
                            MenuItem item = (MenuItem) iMenuItem;
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
                                int size = resultSet.getInt("size");
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

    private IMenuItem mapper(ResultSet resultSet) throws SQLException {
        return new MenuItem(resultSet.getLong("id"), resultSet.getDouble("price"),
                resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("creation_date").toLocalDateTime(),
                resultSet.getInt("version"), resultSet.getLong("menu_id"));
    }
}
