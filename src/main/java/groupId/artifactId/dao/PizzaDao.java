package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaDao;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.IPizza;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaDao implements IPizzaDao {
    private static final String INSERT_PIZZA_SQL = "INSERT INTO pizza_manager.pizza (completed_order_id,name, size) "
            + "VALUES (?, ?, ?);";
    private static final String SELECT_PIZZA_BY_ID_SQL = "SELECT id, completed_order_id, name, size, creation_date, version" +
            " FROM pizza_manager.pizza WHERE id=?;";
    private static final String SELECT_PIZZA_SQL = "SELECT id, completed_order_id, name, size, creation_date, version " +
            "FROM pizza_manager.pizza ORDER BY id;";
    private final DataSource dataSource;

    public PizzaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IPizza save(IPizza pizza) {
        if (pizza.getId() != null || pizza.getVersion() != null) {
            throw new IllegalStateException("Pizza id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_PIZZA_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, pizza.getCompletedOrderId());
                statement.setString(2, pizza.getName());
                statement.setInt(3, pizza.getSize());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect pizza table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new Pizza(generatedKeys.getLong(1), pizza.getCompletedOrderId(),
                            pizza.getName(), pizza.getSize());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new Pizza");
        }
    }

    @Override
    public List<IPizza> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IPizza> iPizzaInfos = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        iPizzaInfos.add(this.mapper(resultSet));
                    }
                }
                return iPizzaInfos;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of pizza Info");
        }
    }

    @Override
    public IPizza get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Pizza by id:" + id);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Pizza with id:" + id);
        }
    }

    private IPizza mapper(ResultSet resultSet) throws SQLException {
        return new Pizza(resultSet.getLong("id"), resultSet.getLong("completed_order_id"),
                resultSet.getString("name"), resultSet.getInt("size"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }
}
