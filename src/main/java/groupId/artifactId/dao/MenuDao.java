package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.exceptions.IncorrectDataSourceException;
import groupId.artifactId.exceptions.IncorrectDeleteConditionsException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MenuDao implements IMenuDao {
    private static MenuDao firstInstance = null;
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

    public MenuDao() {

        try {
            this.dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class at MenuDao", e);
        }
    }

    public static MenuDao getInstance() {
        synchronized (MenuDao.class) {
            if (firstInstance == null) {
                firstInstance = new MenuDao();
            }
        }
        return firstInstance;
    }

    @Override
    public List<IMenu> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenu> menus = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Menu menu = new Menu();
                        menu.setId(resultSet.getLong("id"));
                        menu.setCreationDate(resultSet.getTimestamp("created_at").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("version"));
                        menu.setName(resultSet.getString("name"));
                        menu.setEnable(resultSet.getBoolean("enabled"));
                        menus.add(menu);
                    }
                }
            }
            return menus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IMenu get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            Menu menu = new Menu();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menu.setId(resultSet.getLong("id"));
                        menu.setCreationDate(resultSet.getTimestamp("created_at").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("version"));
                        menu.setName(resultSet.getString("name"));
                        menu.setEnable(resultSet.getBoolean("enabled"));
                    }
                    return menu;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(IMenu menu) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_MENU_SQL)) {
                long rows = 0;
                statement.setString(1, menu.getName());
                statement.setBoolean(2, menu.getEnable());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table insert failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IMenu menu, Long id, Integer version) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        if (!this.getVersion(id, version).equals(version)) {
            throw new IllegalArgumentException("Version " + version + "does not match. Update denied");
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
                    throw new SQLException("menu table update failed, no rows affected");
                }
            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        if (!this.getVersion(id, version).equals(version) && !delete) {
            throw new IncorrectDeleteConditionsException("Version " + version + "does not match. Delete anyway?");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_MENU_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu table delete failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public Integer getVersion(Long id, Integer version) {
        int menuVersion = 0;
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menuVersion = resultSet.getInt("version");
                    }
                }
            }
            return menuVersion;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
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
}
