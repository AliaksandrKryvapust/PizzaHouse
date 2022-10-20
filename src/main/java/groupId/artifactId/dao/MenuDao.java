package groupId.artifactId.dao;

import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.test.Employ;
import groupId.artifactId.exceptions.IncorrectDataSourceException;
import groupId.artifactId.exceptions.IncorrectSQLConnectionException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MenuDao implements IMenuDao {

    private DataSource dataSource;

    public MenuDao() {
    }

    @Override
    public List<IMenu> get() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT id, menu_item_id, price, pizza_info_id," +
                        "name, description, size\n FROM menu\n " +
                        "INNER JOIN menu_item ON menu.menu_item_id=menu_item.id\n" +
                        "INNER JOIN pizza_info ON menu_item.pizza_info_id=pizza_info.id;")) {
                    List<IMenu> list = new ArrayList<>();

                    while (resultSet.next()) {
                        list.add(MenuMapper.menuMapping(resultSet));
                    }
                    return list;
                }
            }
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to connect to DB", e);
        }
    }

    @Override
    public void add(IMenu menu) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_info (name, description, size)\n VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                int rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setString(1, item.getInfo().getName());
                    statement.setString(2, item.getInfo().getDescription());
                    statement.setInt(3, Math.toIntExact(item.getInfo().getSize())); //check
                    rows += statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        while (generatedKeys.next()) {
                            for (MenuItem i : menu.getItems()) {
                                i.getInfo().setId(generatedKeys.getLong(1));
                            }
                        }
                    }
                }
                if (rows == 0) {
                    throw new SQLException("pizza_info table update failed, no rows affected");
                }
            }
            try (Statement statement = con.createStatement()) {
                for (MenuItem item : menu.getItems()) {
                    statement.executeUpdate("INSERT INTO pizza_info (name, description, size)\n " +
                            "VALUES ('" + item.getInfo().getName() + "', '" + item.getInfo().getDescription() + "', '"
                            + item.getInfo().getSize() + "')");
                    statement.getGeneratedKeys();
                    statement.executeUpdate("INSERT INTO menu_item (price, pizza_info_id)\n " +
                            "SELECT " + item.getPrice() + ", id\n FROM pizza_info\n WHERE name='" +
                            item.getInfo().getName() + "'");

                    statement.executeUpdate("INSERT INTO menu (menu_item_id)\n " +
                            "SELECT id\n FROM menu_item\n INNER JOIN pizza_info\n ON " +
                            "menu_item.pizza_info_id=pizza_info.id WHERE name='" +
                            item.getInfo().getName() + "'");
                }
            }
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to connect to DB", e);
        }
        if (menu.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        Menu temp = (Menu) menu;
        temp.setId(menuList.size() + 1);
        this.menuList.add(temp);
    }

    @Override
    public void save(IMenu menu) throws SQLException {
        Menu old = (Menu) this.menuList.get(menu.getId() - 1);
        Menu temp = (Menu) menu;
        temp.setId(old.getId());
        this.menuList.set(old.getId() - 1, temp);
    }

    @Override
    public void update(IMenuItem menuItem, int menuId) {
        try {
            Objects.requireNonNull(this.get(menuId).orElse(null)).getItems().add(menuItem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IMenu get(Serializable id) throws SQLException {
        return this.menuList.stream().filter((i) -> i.getId() == id).findFirst();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        return 0;
    }

    @Override
    public Boolean isIdExist(int id) {
        return this.menuList.stream().anyMatch((i) -> i.getId() == id);
    }

    @Override
    public Boolean isDishExist(String name) {
        boolean temp = false;
        for (IMenu menu : menuList) {
            temp = menu.getItems().stream().anyMatch((i) -> i.getInfo().getName().equals(name));
        }

        return temp;
    }

}
