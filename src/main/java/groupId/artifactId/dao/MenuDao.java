package groupId.artifactId.dao;

import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.IncorrectSQLConnectionException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MenuDao implements IMenuDao {

    private DataSource dataSource;
    private static Long id = 1L;

    public MenuDao() {
    }

    @Override
    public List<IMenu> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenu> menus = new ArrayList<>();
            for (int i = 0; i < this.id; i++) {
                String sql = "SELECT COUNT(menu_item_id) AS count FROM menu\n" +
                        "WHERE id=?\n GROUP BY id\n ORDER BY count";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setLong(1, i + 1);
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()){
                        List<MenuItem> items = new ArrayList<>();
                        for (int j = 0; j < resultSet.getLong("count"); j++) {
                            items.add(new MenuItem());
                        }
                        menus.add(new Menu(items,(long) (i + 1)));
                    } else {
                        throw new SQLException("SELECT COUNT(menu_item_id) failed, no data returned");
                    }
                }
                }
            }
            String sql = "SELECT id, menu_item_id, price, pizza_info_id, name, description, size\n FROM menu\n" +
                    "INNER JOIN menu_item ON menu.menu_item_id=menu_item.id\n" +
                    "INNER JOIN pizza_info ON menu_item.pizza_info_id=pizza_info.id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    for (IMenu menu : menus) {
                        if (menu.getId() == resultSet.getLong("id")) {

                        }
                    }

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
    public void save(IMenu menu) {
        if (menu.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_info (name, description, size)\n VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setString(1, item.getInfo().getName());
                    statement.setString(2, item.getInfo().getDescription());
                    statement.setLong(3, item.getInfo().getSize()); //check
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
            String menuItemSql = "INSERT INTO menu_item (price, pizza_info_id)\n VALUES (?, ?)";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setDouble(1, item.getPrice());
                    statement.setLong(2, item.getInfo().getId());
                    rows += statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        while (generatedKeys.next()) {
                            for (MenuItem i : menu.getItems()) {
                                i.setId(generatedKeys.getLong(1));
                            }
                        }
                    }
                }
                if (rows == 0) {
                    throw new SQLException("menu_item table update failed, no rows affected");
                }
            }
            String menuSql = "INSERT INTO menu (id, menu_item_id)\n VALUES (?, ?)";
            try (PreparedStatement statement = con.prepareStatement(menuSql)) {
                long rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setLong(1, id);
                    statement.setLong(2, item.getId());
                    rows += statement.executeUpdate();
                }
                if (rows == 0) {
                    throw new SQLException("menu table update failed, no rows affected");
                }
            }
            id++;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(MenuItem menuItem, int menuId) {
        if (this.isIdExist((long) menuId)) {
            try (Connection con = dataSource.getConnection()) {
                String pizzaInfoSql = "INSERT INTO pizza_info (name, description, size)\n VALUES (?, ?, ?)";
                try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                    long rows = 0;
                    statement.setString(1, menuItem.getInfo().getName());
                    statement.setString(2, menuItem.getInfo().getDescription());
                    statement.setLong(3, menuItem.getInfo().getSize()); //check
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("pizza_info table update failed, no rows affected");
                    }
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            menuItem.getInfo().setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("pizza_info table update failed, no generated id returned");
                        }
                    }
                }
                String menuItemSql = "INSERT INTO menu_item (price, pizza_info_id)\n VALUES (?, ?)";
                try (PreparedStatement statement = con.prepareStatement(menuItemSql, Statement.RETURN_GENERATED_KEYS)) {
                    long rows = 0;
                    statement.setDouble(1, menuItem.getPrice());
                    statement.setLong(2, menuItem.getInfo().getId());
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("menu_item table update failed, no rows affected");
                    }
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            menuItem.setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("menu_item table update failed, no generated id returned");
                        }
                    }
                }
                String menuSql = "INSERT INTO menu (id, menu_item_id)\n VALUES (?, ?)";
                try (PreparedStatement statement = con.prepareStatement(menuSql)) {
                    long rows = 0;
                    statement.setLong(1, menuId);
                    statement.setLong(2, menuItem.getId());
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("menu table update failed, no rows affected");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalStateException("Error code 500. Menu id is not valid");
    }

    @Override
    public IMenu get(Serializable id) {
        if (!this.isIdExist((Long) id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        return this.menuList.stream().filter((i) -> i.getId() == id).findFirst();
    }

    @Override
    public void delete(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String sql = "DELETE FROM menu \n VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setString(1, menuItem.getInfo().getName());
                statement.setString(2, menuItem.getInfo().getDescription());
                statement.setLong(3, menuItem.getInfo().getSize()); //check
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table update failed, no rows affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        menuItem.getInfo().setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("pizza_info table update failed, no generated id returned");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        @Override
        public Boolean isIdExist (Long id){
            return this.menuList.stream().anyMatch((i) -> i.getId() == id);
        }

        @Override
        public Boolean isDishExist (String name){
            boolean temp = false;
            for (IMenu menu : menuList) {
                temp = menu.getItems().stream().anyMatch((i) -> i.getInfo().getName().equals(name));
            }

            return temp;
        }

    }
