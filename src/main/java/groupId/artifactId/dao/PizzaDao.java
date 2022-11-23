package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaDao;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.PIZZA_FK;

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
    public IPizza save(IPizza pizza, EntityManager entityTransaction) {
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
            if (e.getMessage().contains(PIZZA_FK)) {
                throw new NoContentException("pizza table insert failed, check preconditions and FK values: " + pizza);
            } else {
                throw new DaoException("Failed to save new Pizza" + pizza, e);
            }
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
        } catch (Exception e) {
            throw new DaoException("Failed to get List of pizza Info", e);
        }
    }

    @Override
    public IPizza get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    if (!resultSet.isLast()) {
                        throw new NoContentException("There is no Pizza with id:" + id);
                    }
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Pizza by id:" + id, e);
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
        } catch (Exception e) {
            throw new DaoException("Failed to select Pizza with id:" + id, e);
        }
    }

    private IPizza mapper(ResultSet resultSet) throws SQLException {
        return new Pizza(resultSet.getLong("id"), resultSet.getLong("completed_order_id"),
                resultSet.getString("name"), resultSet.getInt("size"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }
}
