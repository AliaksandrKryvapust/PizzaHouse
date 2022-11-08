package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IOrderDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements IOrderDao {

    private static final String INSERT_ORDER_SQL = "INSERT INTO pizza_manager.order_table DEFAULT VALUES;";
    private static final String SELECT_ORDER_SQL = "SELECT id, creation_date, version " +
            "FROM pizza_manager.order_table ORDER BY id;";
    private static final String SELECT_ORDER_BY_ID_SQL = "SELECT id, creation_date, version " +
            "FROM pizza_manager.order_table WHERE id=?;";
    private static final String SELECT_ORDER_ALL_DATA_SQL = "SELECT ord.id AS id, ord.creation_date AS cd, " +
            "ord.version AS ver, si.id AS siid, menu_item_id, count ,si.creation_date AS sicd, si.version AS siiv," +
            "mi.id AS miid, price, pizza_info_id, mi.creation_date AS micd, mi.version AS miver, mi.menu_id AS meid, name, description, size, " +
            "pi.creation_date AS picd, pi.version AS piv FROM pizza_manager.order_table AS ord " +
            "INNER JOIN pizza_manager.selected_item si on ord.id = si.order_id INNER JOIN menu_item mi on mi.id = si.menu_item_id" +
            " JOIN pizza_info pi on pi.id = mi.pizza_info_id WHERE ord.id=? ORDER BY siid, miid, pizza_info_id;";
    private static final String DELETE_ORDER_SQL = "DELETE FROM pizza_manager.order_table WHERE id=? AND version=?;";
    private final DataSource dataSource;

    public OrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IOrder save(IOrder order) {
        if (order.getId() != null || order.getVersion() != null) {
            throw new IllegalStateException("Order id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("order table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect order table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new Order(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new Order");
        }
    }

    @Override
    public List<IOrder> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IOrder> iOrders = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        iOrders.add(this.mapper(resultSet));
                    }
                }
                return iOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of orders");
        }
    }

    @Override
    public IOrder get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order by id:" + id);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_ORDER_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("order table delete failed,version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect order table delete, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order with id:" + id);
        }
    }

    @Override
    public IOrder getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return this.menuItemMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order with selected items by id:" + id);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Order with id:" + id);
        }
    }

    private IOrder mapper(ResultSet resultSet) throws SQLException {
        return new Order(resultSet.getLong("id"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }

    private IOrder menuItemMapper(ResultSet resultSet) throws SQLException {
        List<ISelectedItem> items = new ArrayList<>();
        IOrder order = new Order();
        while (resultSet.next()) {
            IPizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                    resultSet.getString("description"), resultSet.getInt("size"),
                    resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piv"));
            IMenuItem menuItem = new MenuItem(resultSet.getLong("miid"), pizzaInfo, resultSet.getDouble("price"),
                    resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("micd").toInstant(),
                    resultSet.getInt("miver"), resultSet.getLong("meid"));
            ISelectedItem selectedItem = new SelectedItem(menuItem, resultSet.getLong("siid"),
                    resultSet.getLong("menu_item_id"), resultSet.getLong("id"), resultSet.getInt("count"),
                    resultSet.getTimestamp("sicd").toInstant(), resultSet.getInt("siiv"));
            items.add(selectedItem);
            if (!resultSet.isLast()) {
                order = new Order(items, resultSet.getLong("id"), resultSet.getTimestamp("cd").toInstant(),
                        resultSet.getInt("ver"));
            }
        }
        return order;
    }
}


