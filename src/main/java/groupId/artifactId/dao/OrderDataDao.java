package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class OrderDataDao implements IOrderDataDao {
    private static final String INSERT_ORDER_DATA_SQL = "INSERT INTO pizza_manager.order_data (ticket_id,done)\n VALUES (?, ?)";
    private static final String SELECT_ORDER_DATA_SQL = "SELECT id, ticket_id, done, creation_date, version " +
            "FROM pizza_manager.order_data ORDER BY id;";
    private static final String SELECT_ORDER_DATA_BY_ID_SQL = "SELECT id, ticket_id, done, creation_date, version " +
            "FROM pizza_manager.order_data WHERE id=?;";
    private static final String SELECT_ORDER_DATA_BY_TICKET_ID_SQL = "SELECT id, ticket_id, done, creation_date, version " +
            "FROM pizza_manager.order_data WHERE ticket_id=?;";
    private static final String SELECT_ORDER_DATA_ALL_DATA_SQL = "SELECT order_data.id AS id, order_data.ticket_id AS odti, " +
            "done, order_data.creation_date AS odcd , order_data.version AS ver, os.id AS osid, os.description AS osd, " +
            "os.creation_date AS oscd, os.version AS osver, t.order_id AS tid, t.creation_date AS tcd, t.version AS tver, " +
            "ot.creation_date AS ocd, ot.version AS over, si.id AS siid, menu_item_id, count, si.creation_date AS sicd, " +
            "si.version AS siver, price, pizza_info_id, mi.creation_date AS micd, mi.version AS miver, menu_id, name, " +
            "pi.description AS pid, size, pi.creation_date AS picd, pi.version AS piver " +
            "FROM pizza_manager.order_data INNER JOIN  pizza_manager.order_stage os on order_data.id = os.order_data_id " +
            "INNER JOIN pizza_manager.ticket t on t.id = order_data.ticket_id INNER JOIN pizza_manager.order_table ot on ot.id = t.order_id " +
            "INNER JOIN pizza_manager.selected_item si on ot.id = si.order_id INNER JOIN pizza_manager.menu_item mi on mi.id = si.menu_item_id " +
            "INNER JOIN pizza_manager.pizza_info pi on pi.id = mi.pizza_info_id  WHERE order_data.ticket_id=? " +
            "ORDER BY id, odti, osid, tid, siid, menu_item_id, pizza_info_id;";
    private static final String UPDATE_ORDER_DATA_SQL = "UPDATE pizza_manager.order_data SET version=version+1, done=? " +
            "WHERE id=? AND version=?";
    private final DataSource dataSource;

    public OrderDataDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IOrderData save(IOrderData orderData) {
        if (orderData.getId() != null || orderData.getVersion() != null) {
            throw new IllegalStateException("OrderData id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_ORDER_DATA_SQL, RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, orderData.getTicketId());
                statement.setBoolean(2, orderData.isDone());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("order data table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect order data table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new OrderData(generatedKeys.getLong(1), orderData.getTicketId(), orderData.isDone());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new Order data");
        }
    }

    @Override
    public List<IOrderData> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IOrderData> orderData = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        orderData.add(this.mapper(resultSet));
                    }
                }
            }
            return orderData;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of Order Data");
        }
    }

    @Override
    public IOrderData get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order Data by id:" + id);
        }
    }

    @Override
    public IOrderData update(IOrderData orderData, Long id, Integer version) {
        if (orderData.getId() != null || orderData.getVersion() != null) {
            throw new IllegalStateException("Order Data id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(UPDATE_ORDER_DATA_SQL)) {
                long rows = 0;
                statement.setBoolean(1, orderData.isDone());
                statement.setLong(2, id);
                statement.setInt(3, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new IllegalArgumentException("order data table update failed, version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect order data table update, more than 1 row affected");
                }
                return new OrderData(id, orderData.isDone());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update Order data with id:" + id);
        }
    }

    @Override
    public IOrderData getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return this.menuItemMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order Stage by Ticket id:" + id);
        }
    }

    @Override
    public IOrderData getDataByTicket(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_BY_TICKET_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order Data by ticket id:" + id);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Order Data with id:" + id);
        }
    }

    @Override
    public Boolean doesTicketExist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_DATA_BY_TICKET_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Order Data with ticket id:" + id);
        }
    }

    private IOrderData mapper(ResultSet resultSet) throws SQLException {
        List<IOrderStage> stages = new ArrayList<>();
        return new OrderData(new Ticket(), stages, resultSet.getLong("id"), resultSet.getLong("ticket_id"),
                resultSet.getBoolean("done"), resultSet.getTimestamp("creation_date").toInstant(),
                resultSet.getInt("version"));
    }

    private IOrderData menuItemMapper(ResultSet resultSet) throws SQLException {
        List<IOrderStage> stages = new ArrayList<>();
        List<ISelectedItem> items = new ArrayList<>();
        IOrder order;
        ITicket ticket;
        IOrderData orderData = new OrderData();
        while (resultSet.next()) {
            IPizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                    resultSet.getString("pid"), resultSet.getInt("size"),
                    resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piver"));
            IMenuItem menuItem = new MenuItem(resultSet.getLong("menu_item_id"), pizzaInfo, resultSet.getDouble("price"),
                    resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("micd").toInstant(),
                    resultSet.getInt("miver"), resultSet.getLong("menu_id"));
            ISelectedItem selectedItem = new SelectedItem(menuItem, resultSet.getLong("siid"),
                    resultSet.getLong("menu_item_id"), resultSet.getLong("tid"), resultSet.getInt("count"),
                    resultSet.getTimestamp("sicd").toInstant(), resultSet.getInt("siver"));
            items.add(selectedItem);
            IOrderStage orderStage = new OrderStage(resultSet.getLong("osid"), resultSet.getLong("id"),
                    resultSet.getString("osd"), resultSet.getTimestamp("oscd").toInstant(),
                    resultSet.getInt("osver"));
            stages.add(orderStage);
            if (resultSet.isLast()) {
                order = new Order(items, resultSet.getLong("tid"), resultSet.getTimestamp("ocd").toInstant(),
                        resultSet.getInt("over"));
                ticket = new Ticket(order, resultSet.getLong("odti"), resultSet.getLong("tid"),
                        resultSet.getTimestamp("tcd").toInstant(), resultSet.getInt("tver"));
                orderData = new OrderData(ticket, stages, resultSet.getLong("id"), resultSet.getLong("odti"),
                        resultSet.getBoolean("done"), resultSet.getTimestamp("odcd").toInstant(),
                        resultSet.getInt("ver"));
            }
        }
        return orderData;
    }
}


