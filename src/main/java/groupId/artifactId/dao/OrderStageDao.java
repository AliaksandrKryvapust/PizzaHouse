package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IOrderStageDao;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.IOrderStage;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStageDao implements IOrderStageDao {
    private static final String INSERT_ORDER_STAGE_SQL = "INSERT INTO pizza_manager.order_stage (order_data_id, description) " +
            "VALUES (?, ?);";
    private static final String SELECT_ORDER_STAGE_SQL = "SELECT id, order_data_id, description, creation_date, version " +
            "FROM pizza_manager.order_stage ORDER BY id;";
    private static final String SELECT_ORDER_STAGE_BY_ID_SQL = "SELECT id, order_data_id, description, creation_date, " +
            "version FROM pizza_manager.order_stage WHERE id=?;";
    private static final String SELECT_ORDER_STAGE_BY_DESCRIPTION_SQL = "SELECT description FROM pizza_manager.order_stage " +
            "WHERE description=?;";
    private final DataSource dataSource;

    public OrderStageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IOrderStage save(IOrderStage orderStage) {
        if (orderStage.getId() != null || orderStage.getVersion() != null) {
            throw new IllegalStateException("Order Stage id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_ORDER_STAGE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, orderStage.getOrderDataId());
                statement.setString(2, orderStage.getDescription());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("order_stage table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect order_stage table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new OrderStage(generatedKeys.getLong(1), orderStage.getOrderDataId(),
                            orderStage.getDescription());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new Order Stage");
        }
    }

    @Override
    public List<IOrderStage> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IOrderStage> stages = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_STAGE_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        stages.add(this.mapper(resultSet));
                    }
                }
                return stages;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of Order stages");
        }
    }

    @Override
    public IOrderStage get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_STAGE_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Order stage by id:" + id);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_STAGE_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Order stage with id:" + id);
        }
    }

    @Override
    public Boolean doesStageExist(String description) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_ORDER_STAGE_BY_DESCRIPTION_SQL)) {
                statement.setString(1, description);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Order Stage with name:" + description);
        }
    }

    private IOrderStage mapper(ResultSet resultSet) throws SQLException {
        return new OrderStage(resultSet.getLong("id"), resultSet.getLong("order_data_id"),
                resultSet.getString("description"),
                resultSet.getTimestamp("creation_date").toLocalDateTime(), resultSet.getInt("version"));
    }
}
