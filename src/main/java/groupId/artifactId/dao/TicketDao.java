package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    private static final String DELETE_TICKET_SQL = "DELETE FROM pizza_manager.ticket WHERE id=? AND version=?;";
    private final DataSource dataSource;

    public TicketDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ITicket save(ITicket ticket) {
        if (ticket.getId() != null || ticket.getVersion() != null) {
            throw new IllegalStateException("Ticket id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, ticket.getOrderId());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("ticket table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect ticket table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new Ticket(generatedKeys.getLong(1), ticket.getOrderId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new Ticket");
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of tickets");
        }
    }

    @Override
    public ITicket get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Ticket by id:" + id);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_TICKET_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("ticket table delete failed,version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect ticket table delete, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order with id:" + id);
        }
    }

    @Override
    public ITicket getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_TICKET_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return this.menuItemMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Ticket with selected items by id:" + id);
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select ticket with id:" + id);
        }
    }

    private ITicket mapper(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getLong("id"), resultSet.getLong("order_id"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }

    private ITicket menuItemMapper(ResultSet resultSet) throws SQLException {
        List<ISelectedItem> items = new ArrayList<>();
        IOrder order;
        ITicket ticket = new Ticket();
        while (resultSet.next()) {
            IPizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                    resultSet.getString("description"), resultSet.getInt("size"),
                    resultSet.getTimestamp("picd").toLocalDateTime(), resultSet.getInt("piv"));
            IMenuItem menuItem = new MenuItem(resultSet.getLong("miid"), pizzaInfo, resultSet.getDouble("price"),
                    resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("micd").toInstant(),
                    resultSet.getInt("miver"), resultSet.getLong("meid"));
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
        return ticket;
    }
}
