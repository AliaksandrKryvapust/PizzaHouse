package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.COMPLETED_ORDER_FK;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CompletedOrderDao implements ICompletedOrderDao {
    private static final String INSERT_COMPLETED_ORDER_SQL = "INSERT INTO pizza_manager.completed_order (ticket_id) VALUES (?)";
    private static final String SELECT_COMPLETED_ORDER_SQL = "SELECT id, ticket_id, creation_date, version " +
            "FROM pizza_manager.completed_order ORDER BY id;";
    private static final String SELECT_COMPLETED_ORDER_BY_ID_SQL = "SELECT id, ticket_id, creation_date, version " +
            "FROM pizza_manager.completed_order WHERE id=?;";
    private static final String SELECT_COMPLETED_ORDER_ALL_DATA_SQL = "SELECT completed_order.id AS id, completed_order.ticket_id AS coti, " +
            "completed_order.creation_date AS cocd , completed_order.version AS ver, pizza.id AS pizzaid, pizza.name AS pn,  pizza.size AS ps," +
            "pizza.creation_date AS pcd, pizza.version AS pver, t.order_id AS tid, t.creation_date AS tcd, t.version AS tver, " +
            "ot.creation_date AS ocd, ot.version AS over, si.id AS siid, menu_item_id, count, si.creation_date AS sicd, " +
            "si.version AS siver, price, pizza_info_id, mi.creation_date AS micd, mi.version AS miver, menu_id, pi.name AS pin, " +
            "pi.description AS pid, pi.size AS pis, pi.creation_date AS picd, pi.version AS piver " +
            "FROM pizza_manager.completed_order INNER JOIN  pizza_manager.pizza ON completed_order.id = pizza.completed_order_id " +
            "INNER JOIN pizza_manager.ticket t on t.id = completed_order.ticket_id INNER JOIN pizza_manager.order_table ot on ot.id = t.order_id " +
            "INNER JOIN pizza_manager.selected_item si on ot.id = si.order_id INNER JOIN pizza_manager.menu_item mi on mi.id = si.menu_item_id " +
            "INNER JOIN pizza_manager.pizza_info pi on pi.id = mi.pizza_info_id  WHERE completed_order.ticket_id=? " +
            "ORDER BY id, pizzaid, tid, siid, menu_item_id, pizza_info_id;";
    private final DataSource dataSource;

    public CompletedOrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ICompletedOrder getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_COMPLETED_ORDER_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return this.allDataMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Completed Order by Ticket id:" + id, e);
        }
    }

    @Override
    public ICompletedOrder save(ICompletedOrder completedOrder, EntityManager entityTransaction) {
        if (completedOrder.getId() != null || completedOrder.getVersion() != null) {
            throw new IllegalStateException("CompletedOrder id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_COMPLETED_ORDER_SQL, RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, completedOrder.getTicketId());
                rows += statement.executeUpdate();
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect completed order table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new CompletedOrder(generatedKeys.getLong(1), completedOrder.getTicketId());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains(COMPLETED_ORDER_FK)) {
                throw new NoContentException("completed order table insert failed, check preconditions and FK values: "
                + completedOrder);
            } else {
                throw new DaoException("Failed to save new Completed Order:" + completedOrder, e);
            }
        }
    }

    @Override
    public List<ICompletedOrder> get() {
        try (Connection con = dataSource.getConnection()) {
            List<ICompletedOrder> orderData = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_COMPLETED_ORDER_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        orderData.add(this.mapper(resultSet));
                    }
                }
            }
            return orderData;
        } catch (Exception e) {
            throw new DaoException("Failed to get List of Completed Order", e);
        }
    }

    @Override
    public ICompletedOrder get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_COMPLETED_ORDER_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    if (!resultSet.isLast()) {
                        throw new NoContentException("There is no Completed Order with id:" + id);
                    }
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Completed Order by id:" + id, e);
        }
    }

    private ICompletedOrder mapper(ResultSet resultSet) throws SQLException {
        List<IPizza> stages = new ArrayList<>();
        return new CompletedOrder(new Ticket(), stages, resultSet.getLong("id"), resultSet.getLong("ticket_id"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }

    private ICompletedOrder allDataMapper(ResultSet resultSet) throws SQLException {
        List<IPizza> pizzas = new ArrayList<>();
        List<ISelectedItem> items = new ArrayList<>();
        IOrder order;
        ITicket ticket;
        ICompletedOrder completedOrder = new CompletedOrder();
        while (resultSet.next()) {
            PizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("pin"),
                    resultSet.getString("pid"), resultSet.getInt("pis"),
                    resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piver"));
            IMenuItem menuItem = new MenuItem(resultSet.getLong("menu_item_id"), pizzaInfo, resultSet.getDouble("price"),
                    resultSet.getTimestamp("micd").toInstant(), resultSet.getInt("miver"));
            ISelectedItem selectedItem = new SelectedItem( resultSet.getLong("siid"),menuItem,
                    resultSet.getLong("tid"), resultSet.getInt("count"),
                    resultSet.getTimestamp("sicd").toInstant(), resultSet.getInt("siver"));
            items.add(selectedItem);
            IPizza pizza = new Pizza(resultSet.getLong("pizzaid"), resultSet.getLong("id"),
                    resultSet.getString("pn"), resultSet.getInt("ps"),
                    resultSet.getTimestamp("pcd").toInstant(), resultSet.getInt("pver"));
            pizzas.add(pizza);
            if (resultSet.isLast()) {
                order = new Order(items, resultSet.getLong("tid"), resultSet.getTimestamp("ocd").toInstant(),
                        resultSet.getInt("over"));
                ticket = new Ticket(order, resultSet.getLong("coti"), resultSet.getLong("tid"),
                        resultSet.getTimestamp("tcd").toInstant(), resultSet.getInt("tver"));
                completedOrder = new CompletedOrder(ticket, pizzas, resultSet.getLong("id"), resultSet.getLong("coti"),
                        resultSet.getTimestamp("cocd").toInstant(),
                        resultSet.getInt("ver"));
            }
        }
        if (completedOrder.getItems() == null || completedOrder.getTicket() == null || completedOrder.getId() == null) {
            throw new NoContentException("There is no Completed Order with such id");
        }
        return completedOrder;
    }
}


