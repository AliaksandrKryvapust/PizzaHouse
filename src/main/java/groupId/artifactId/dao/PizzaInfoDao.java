package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaInfoDao implements IPizzaInfoDao {
    private final DataSource dataSource;
    private static final String INSERT_PIZZA_INFO_SQL = "INSERT INTO pizza_manager.pizza_info (name, description, size) " +
            "VALUES (?, ?, ?);";
    private static final String UPDATE_PIZZA_INFO_SQL = "UPDATE pizza_manager.pizza_info SET name=?, description=?, size=?, " +
            "version=version+1 WHERE id=? AND version=?;";
    private static final String SELECT_PIZZA_INFO_SQL = "SELECT id, name, description, size, creation_date, version " +
            "FROM pizza_manager.pizza_info ORDER BY id;";
    private static final String SELECT_PIZZA_INFO_BY_ID_SQL = "SELECT id, name, description, size, creation_date, " +
            "version FROM pizza_manager.pizza_info WHERE id=?;";
    private static final String SELECT_PIZZA_INFO_BY_NAME_SQL = "SELECT name FROM pizza_manager.pizza_info WHERE name=?;";
    private static final String DELETE_PIZZA_INFO_SQL = "DELETE FROM pizza_manager.pizza_info WHERE id=? AND version=?;";

    public PizzaInfoDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IPizzaInfo save(IPizzaInfo info) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_PIZZA_INFO_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setString(1, info.getName());
                statement.setString(2, info.getDescription());
                statement.setLong(3, info.getSize());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect pizza_info table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new PizzaInfo(Long.valueOf(generatedKeys.getString(1)), info.getName(),
                            info.getDescription(), info.getSize());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save new PizzaInfo");
        }
    }

    @Override
    public IPizzaInfo update(IPizzaInfo info, Long id, Integer version) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(UPDATE_PIZZA_INFO_SQL)) {
                long rows = 0;
                statement.setString(1, info.getName());
                statement.setString(2, info.getDescription());
                statement.setLong(3, info.getSize());
                statement.setLong(4, id);
                statement.setInt(5, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new IllegalArgumentException("pizza_info table update failed, version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect pizza_info table update, more than 1 row affected");
                }
                return new PizzaInfo(id, info.getName(), info.getDescription(), info.getSize());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update pizza_info by id:" + id);
        }
    }

    @Override
    public IPizzaInfo get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Pizza Info by id:" + id);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_PIZZA_INFO_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table delete failed,version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect pizza_info table update, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete pizza info with id:" + id);
        }
    }

    @Override
    public List<IPizzaInfo> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IPizzaInfo> iPizzaInfos = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_SQL)) {
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
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Pizza Info with id:"+ id);
        }
    }

    @Override
    public Boolean doesPizzaExist(String name) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_NAME_SQL)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to select Pizza Info with name:"+ name);
        }
    }

    private IPizzaInfo mapper(ResultSet resultSet) throws SQLException {
        return new PizzaInfo(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getLong("size"),
                resultSet.getTimestamp("creation_date").toLocalDateTime(), resultSet.getInt("version"));
    }
}
