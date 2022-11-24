package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.TICKET_FK;

public class TicketDao implements ITicketDao {

    private static final String INSERT_TICKET_SQL = "INSERT INTO pizza_manager.ticket (order_id) VALUES (?);";
    private static final String SELECT_TICKET_SQL = "SELECT id, order_id, creation_date, version " +
            "FROM pizza_manager.ticket ORDER BY id;";
    private static final String SELECT_TICKET_BY_ID_SQL = "SELECT id, order_id,  creation_date, version " +
            "FROM pizza_manager.ticket WHERE id=?;";
    private static final String SELECT_TICKET_ALL_DATA_SQL = "SELECT ord.id AS id, ord.creation_date AS cd, " +
            "ord.version AS ver, t.id AS tid, t.creation_date AS tcd, t.version AS tver,si.id AS siid, menu_item_id, " +
            "count ,si.creation_date AS sicd, si.version AS siiv, mi.id AS miid, price, pizza_info_id, mi.creation_date AS micd, " +
            "mi.version AS miver, mi.menu_id AS meid, name, description, size, pi.creation_date AS picd, pi.version AS piv " +
            "FROM pizza_manager.order_table AS ord INNER JOIN pizza_manager.ticket t on ord.id = t.order_id " +
            "INNER JOIN pizza_manager.selected_item si on ord.id = si.order_id " +
            "INNER JOIN pizza_manager.menu_item mi on mi.id = si.menu_item_id " +
            "INNER JOIN pizza_manager.pizza_info pi on pi.id = mi.pizza_info_id WHERE ord.id=? ORDER BY siid, miid, pizza_info_id;";
    private static final String DELETE_TICKET_SQL = "DELETE FROM pizza_manager.ticket WHERE id=?;";
    private final DataSource dataSource;

    public TicketDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ITicket save(ITicket ticket, EntityManager entityTransaction) {
        if (ticket.getId() != null || ticket.getVersion() != null) {
            throw new IllegalStateException("Ticket id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, ticket.getOrderId());
                rows += statement.executeUpdate();
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect ticket table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new Ticket(generatedKeys.getLong(1), ticket.getOrderId());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains(TICKET_FK)) {
                throw new NoContentException("ticket table insert failed, check preconditions and FK values: " + ticket);
            } else {
                throw new DaoException("Failed to save new Ticket" + ticket, e);
            }
        }
    }

    @Override
    public List<ITicket> get() {
        try (Connection con = dataSource.getConnection()) {
            List<ITicket> iTickets = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        iTickets.add(this.mapper(resultSet));
                    }
                }
                return iTickets;
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of tickets", e);
        }
    }

    @Override
    public ITicket get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    if (!resultSet.isLast()) {
                        throw new NoContentException("There is no Ticket with id:" + id);
                    }
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Ticket by id:" + id, e);
        }
    }

    @Override
    public void delete(Long id, Boolean delete, EntityManager entityTransaction) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_TICKET_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                rows += statement.executeUpdate();
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect ticket table delete, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete order with id:" + id, e);
        }
    }

    @Override
    public ITicket getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return this.allDataMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Ticket with selected items by id:" + id, e);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (Exception e) {
            throw new DaoException("Failed to select ticket with id:" + id, e);
        }
    }

    private ITicket mapper(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getLong("id"), resultSet.getLong("order_id"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }

    private ITicket allDataMapper(ResultSet resultSet) throws SQLException {
        List<ISelectedItem> items = new ArrayList<>();
        IOrder order;
        ITicket ticket = new Ticket();
        while (resultSet.next()) {
            PizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                    resultSet.getString("description"), resultSet.getInt("size"),
                    resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piv"));
            IMenuItem menuItem = new MenuItem(resultSet.getLong("miid"), pizzaInfo, resultSet.getDouble("price"),
                    resultSet.getTimestamp("micd").toInstant(), resultSet.getInt("miver"));
            ISelectedItem selectedItem = new SelectedItem(menuItem, resultSet.getLong("siid"),
                    resultSet.getLong("menu_item_id"), resultSet.getLong("id"), resultSet.getInt("count"),
                    resultSet.getTimestamp("sicd").toInstant(), resultSet.getInt("siiv"));
            items.add(selectedItem);
            if (resultSet.isLast()) {
                order = new Order(items, resultSet.getLong("id"), resultSet.getTimestamp("cd").toInstant(),
                        resultSet.getInt("ver"));
                ticket = new Ticket(order, resultSet.getLong("tid"), resultSet.getLong("id"),
                        resultSet.getTimestamp("tcd").toInstant(), resultSet.getInt("tver"));
            }
        }
        if (ticket.getOrder() == null || ticket.getId() == null) {
            throw new NoContentException("There is no Ticket with such id");
        }
        return ticket;
    }
}
