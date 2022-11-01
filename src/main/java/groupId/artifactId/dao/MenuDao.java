package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class MenuDao implements IMenuDao {
    private final DataSource dataSource;
    private static final String SELECT_MENU_BY_ID_SQL = "SELECT id, created_at, version, name, enabled " +
            "FROM pizza_manager.menu WHERE id=?;";
    private static final String SELECT_MENU_SQL = "SELECT id, created_at, version, name, enabled " +
            "FROM pizza_manager.menu ORDER BY id;";
    private static final String SELECT_MENU_BY_NAME_SQL = "SELECT name FROM pizza_manager.pizza_info WHERE name=?;";
    private static final String INSERT_MENU_SQL = "INSERT INTO pizza_manager.menu (name, enabled)\n VALUES (?, ?)";
    private static final String UPDATE_MENU_SQL = "UPDATE pizza_manager.menu SET version=version+1, name=?, enabled=? " +
            "WHERE id=? AND version=?";
    private static final String DELETE_MENU_SQL = "DELETE FROM pizza_manager.menu WHERE id=? AND version=?;";

    public MenuDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<IMenu> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenu> menus = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menus.add(mapper(resultSet));
                    }
                }
            }
            return menus;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get List of Menu");
        }
    }

    @Override
    public IMenu get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Menu");
        }
    }

    @Override
    public IMenu save(IMenu menu) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_MENU_SQL, RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setString(1, menu.getName());
                statement.setBoolean(2, menu.getEnable());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table insert failed, no rows affected");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new Menu(Long.valueOf(generatedKeys.getString(1)), menu.getName(), menu.getEnable());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save Menu");
        }

    }

    @Override
    public IMenu update(IMenu menu, Long id, Integer version) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(UPDATE_MENU_SQL)) {
                long rows = 0;
                statement.setString(1, menu.getName());
                statement.setBoolean(2, menu.getEnable());
                statement.setLong(3, id);
                statement.setInt(4, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new IllegalArgumentException("menu table update failed, version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu table update, more than 1 row affected");
                }
                return new Menu(id, menu.getName(), menu.getEnable());
            }
            } catch (SQLException e) {
            throw new RuntimeException("Failed to update Menu");
            }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_MENU_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table delete failed,version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu table update, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete Menu");
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean doesMenuExist(String name) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_NAME_SQL)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private IMenu mapper(ResultSet resultSet) throws SQLException {
        return new Menu(resultSet.getLong("id"), resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getInt("version"), resultSet.getString("name"),
                resultSet.getBoolean("enabled"));
    }
}
