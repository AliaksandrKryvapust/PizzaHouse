package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MenuDao implements IMenuDao {

    private DataSource dataSource; // create singleton
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
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            List<MenuItem> items = new ArrayList<>();
                            for (int j = 0; j < resultSet.getLong("count"); j++) {
                                items.add(new MenuItem());
                            }
                            menus.add(new Menu(items, (long) (i + 1)));
                        } else {
                            throw new SQLException("SELECT COUNT(menu_item_id) failed, no data returned");
                        }
                    }
                }
                String sqln = "SELECT menu_item_id FROM menu\n" +
                        "WHERE id=?\n ORDER BY menu_item_id";
                try (PreparedStatement statement = con.prepareStatement(sqln)) {
                    statement.setLong(1, i + 1);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        for (MenuItem item : menus.get(i).getItems()) {
                            resultSet.next();
                            item.setId(resultSet.getLong("menu_item_id"));
                        }
                    }
                }
            }
            String sql = "SELECT id, menu_item_id, price, pizza_info_id, name, description, size\n FROM menu\n" +
                    "INNER JOIN menu_item ON menu.menu_item_id=menu_item.id,\n" +
                    "INNER JOIN pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "ORDER BY id, menu_item_id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        for (IMenu menu : menus) {
                            if (menu.getId() == resultSet.getLong("id")) {
                                for (MenuItem item : menu.getItems()) {
                                    if (item.getId() == resultSet.getLong("menu_item_id")) {
                                        double price = resultSet.getDouble("price");
                                        if (!resultSet.wasNull()) {
                                            item.setPrice(price);
                                        }
                                        long id = resultSet.getLong("pizza_info_id");
                                        if (!resultSet.wasNull()) {
                                            item.getInfo().setId(id);
                                        }
                                        item.getInfo().setName(resultSet.getString("name"));
                                        item.getInfo().setDescription(resultSet.getString("description"));
                                        long size = resultSet.getLong("size");
                                        if (!resultSet.wasNull()) {
                                            item.getInfo().setSize(size);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return menus;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public IMenu get(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            Menu menu = new Menu();
            menu.setId(id);
            String sql = "SELECT menu_item_id, price, pizza_info_id, name, description, size\n FROM menu\n" +
                    "INNER JOIN menu_item ON menu.menu_item_id=menu_item.id,\n" +
                    "INNER JOIN pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "WHERE id=?\n ORDER BY menu_item_id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        for (MenuItem item : menu.getItems()) {
                            if (item.getId() == resultSet.getLong("menu_item_id")) {
                                double price = resultSet.getDouble("price");
                                if (!resultSet.wasNull()) {
                                    item.setPrice(price);
                                }
                                long itemId = resultSet.getLong("pizza_info_id");
                                if (!resultSet.wasNull()) {
                                    item.getInfo().setId(itemId);
                                }
                                item.getInfo().setName(resultSet.getString("name"));
                                item.getInfo().setDescription(resultSet.getString("description"));
                                long size = resultSet.getLong("size");
                                if (!resultSet.wasNull()) {
                                    item.getInfo().setSize(size);
                                }
                            }
                        }
                    }
                    return menu;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "DELETE FROM pizza_info\n INNER JOIN menu_item ON menu_item.pizza_info_id=pizza_info.id,\n" +
                    "INNER JOIN menu ON menu.menu_item_id=menu_item.id\n WHERE menu.id=?;";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                long rows = 0;
                statement.setLong(1, id);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table delete failed, no rows affected"); //check
                }
            }
            String menuItemSql = "DELETE FROM menu_item\n INNER JOIN menu ON menu.menu_item_id=menu_item.id\n" +
                    " WHERE menu.id=?;";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql)) {
                long rows = 0;
                statement.setLong(1, id);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table delete failed, no rows affected");
                }
            }
            String menuSql = "DELETE FROM menu\n WHERE menu.id=?;";
            try (PreparedStatement statement = con.prepareStatement(menuSql)) {
                long rows = 0;
                statement.setLong(1, id);
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
    public Boolean isIdExist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id FROM menu\n WHERE id=?\n ORDER BY id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
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
    public Boolean isDishExist(String name) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT name FROM pizza_info\n WHERE name=?\n ORDER BY name;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
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
