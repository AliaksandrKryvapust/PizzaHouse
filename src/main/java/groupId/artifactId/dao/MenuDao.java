package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MenuDao implements IMenuDao {
    private static MenuDao firstInstance = null;
    private final DataSource dataSource;
    private static Long id;

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
    public List<Menu> get() {
        try (Connection con = dataSource.getConnection()) {
            String sqlId = "SELECT COUNT(DISTINCT id) AS id FROM pizza_manager.menu\n" +
                    "ORDER BY id";
            try (PreparedStatement statement = con.prepareStatement(sqlId)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        id = (resultSet.getLong("id"));
                    }
                }
            }
            List<Menu> menus = new ArrayList<>();
            for (int i = 0; i < id; i++) {
                String sql = "SELECT COUNT(menu_item_id) AS count FROM pizza_manager.menu\n" +
                        "WHERE id=?\n GROUP BY id\n ORDER BY count";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setLong(1, i + 1);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            List<MenuItem> items = new ArrayList<>();
                            for (int j = 0; j < resultSet.getLong("count"); j++) {
                                items.add(new MenuItem(new PizzaInfo()));
                            }
                            menus.add(new Menu(items, (long) (i + 1)));
                        } else {
                            throw new SQLException("SELECT COUNT(menu_item_id) failed, no data returned");
                        }
                    }
                }
                String sqlNew = "SELECT menu_item_id FROM pizza_manager.menu\n" +
                        "WHERE id=?\n ORDER BY menu_item_id";
                try (PreparedStatement statement = con.prepareStatement(sqlNew)) {
                    statement.setLong(1, i + 1);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        for (MenuItem item : menus.get(i).getItems()) {
                            resultSet.next();
                            item.setId(resultSet.getLong("menu_item_id"));
                        }
                    }
                }
            }
            String sql = "SELECT pizza_manager.menu.id, pizza_manager.menu.creation_date AS mcd, pizza_manager.menu.version AS med," +
                    " menu_item_id,  pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version  AS mied, " +
                    "price, pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date AS picd, pizza_manager.pizza_info.version  AS pied\n" +
                    "FROM pizza_manager.menu\n" +
                    "INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id\n" +
                    "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "ORDER BY id, menu_item_id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        for (Menu menu : menus) {
                            if (menu.getId() == resultSet.getLong("id")) {
                                menu.setCreationDate(resultSet.getTimestamp("mcd").toLocalDateTime());
                                menu.setVersion(resultSet.getInt("med"));
                                for (MenuItem item : menu.getItems()) {
                                    if (item.getId() == resultSet.getLong("menu_item_id")) {
                                        double price = resultSet.getDouble("price");
                                        if (!resultSet.wasNull()) {
                                            item.setPrice(price);
                                        }
                                        item.setCreationDate(resultSet.getTimestamp("micd").toLocalDateTime());
                                        item.setVersion(resultSet.getInt("mied"));
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
                                        item.getInfo().setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                                        item.getInfo().setVersion(resultSet.getInt("pied"));
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
//    SELECT pizza_manager.menu.id, pizza_manager.menu.creation_date, pizza_manager.menu.edit_date,
//    menu_item_id,  pizza_manager.menu_item.creation_date, pizza_manager.menu_item.edit_date,
//    price, pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date, pizza_manager.pizza_info.edit_date
//    FROM pizza_manager.menu
//    INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id
//    INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id
//    ORDER BY id, menu_item_id, pizza_info_id;

    @Override
    public IMenu get(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            Menu menu = new Menu();
            menu.setId(id);
            List<MenuItem> list = new ArrayList<>();
            String sql = "SELECT pizza_manager.menu.id,pizza_manager.menu.creation_date AS mcd, pizza_manager.menu.version AS med, " +
                    "menu_item_id, price, pizza_manager.menu_item.creation_date AS micd, pizza_manager.menu_item.version AS mied," +
                    "pizza_info_id, name, description, size, pizza_manager.pizza_info.creation_date AS picd, pizza_manager.pizza_info.version AS pied\n " +
                    "FROM pizza_manager.menu\n INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id\n" +
                    "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
                    "WHERE pizza_manager.menu.id=?\n ORDER BY id, menu_item_id, pizza_info_id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        menu.setCreationDate(resultSet.getTimestamp("mcd").toLocalDateTime());
                        menu.setVersion(resultSet.getInt("med"));
                        MenuItem item = new MenuItem(new PizzaInfo());
                        item.setId(resultSet.getLong("menu_item_id"));
                        double price = resultSet.getDouble("price");
                        if (!resultSet.wasNull()) {
                            item.setPrice(price);
                        }
                        item.setCreationDate(resultSet.getTimestamp("micd").toLocalDateTime());
                        item.setVersion(resultSet.getInt("mied"));
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
                        item.getInfo().setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                        item.getInfo().setVersion(resultSet.getInt("pied"));
                        list.add(item);
                    }
                    menu.setItems(list);
                    return menu;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    SELECT pizza_manager.menu.id, menu_item_id, price, pizza_info_id, name, description, size
//    FROM pizza_manager.menu INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id
//    INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id
//    WHERE pizza_manager.menu.id=1 ORDER BY id, menu_item_id, pizza_info_id;

    //    String sql = "SELECT menu_item_id, price, pizza_info_id, name, description, size\n FROM pizza_manager.menu\n" +
//            "INNER JOIN pizza_manager.menu_item ON menu.menu_item_id=menu_item.id\n" +
//            "INNER JOIN pizza_manager.pizza_info ON menu_item.pizza_info_id=pizza_info.id\n" +
//            "WHERE pizza_manager.menu.id=?\n ORDER BY menu_item_id, pizza_info_id;";
    @Override
    public void save(IMenu menu) {
        if (menu.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?);";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setString(1, item.getInfo().getName());
                    statement.setString(2, item.getInfo().getDescription());
                    statement.setLong(3, item.getInfo().getSize());
                    rows += statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        while (generatedKeys.next()) {
                            item.getInfo().setId(generatedKeys.getLong(1));
                        }
                    }
                }
                if (rows == 0) {
                    throw new SQLException("pizza_info table update failed, no rows affected");
                }
            }
            String menuItemSql = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id)\n VALUES (?, ?);";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                for (MenuItem item : menu.getItems()) {
                    statement.setDouble(1, item.getPrice());
                    statement.setLong(2, item.getInfo().getId());
                    rows += statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        while (generatedKeys.next()) {
                            item.setId(generatedKeys.getLong(1));
                        }
                    }
                }
                if (rows == 0) {
                    throw new SQLException("menu_item table update failed, no rows affected");
                }
            }
            String sqlId = "SELECT COUNT(DISTINCT id) AS id FROM pizza_manager.menu\n" +
                    "ORDER BY id";
            try (PreparedStatement statement = con.prepareStatement(sqlId)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        id = (resultSet.getLong("id"))+1;
                    }
                }
            }
            String menuSql = "INSERT INTO pizza_manager.menu (id, menu_item_id)\n VALUES (?, ?)";
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IMenu iMenu) {

    }

    @Override
    public void add(MenuItem menuItem, Long menuId) {
        if (this.isIdExist(menuId)) {
            try (Connection con = dataSource.getConnection()) {
                String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?)";
                try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql, Statement.RETURN_GENERATED_KEYS)) {
                    long rows = 0;
                    statement.setString(1, menuItem.getInfo().getName());
                    statement.setString(2, menuItem.getInfo().getDescription());
                    statement.setLong(3, menuItem.getInfo().getSize());
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
                String menuItemSql = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id)\n VALUES (?, ?)";
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
                String menuSql = "INSERT INTO pizza_manager.menu (id, menu_item_id)\n VALUES (?, ?)";
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
    public void delete(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. Menu id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "DELETE FROM pizza_manager.pizza_info \n" +
                    "USING pizza_manager.menu_item, pizza_manager.menu\n" +
                    "WHERE menu_item.pizza_info_id=pizza_info.id  AND \n" +
                    "menu.menu_item_id=menu_item.id AND pizza_manager.menu.id=?;";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                long rows = 0;
                statement.setLong(1, id);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table delete failed, no rows affected"); //check
                }
            }
            String menuItemSql = "DELETE FROM  pizza_manager.menu_item\n" +
                    "USING pizza_manager.menu\n" +
                    "WHERE menu.menu_item_id=menu_item.id AND pizza_manager.menu.id=?";
            try (PreparedStatement statement = con.prepareStatement(menuItemSql)) {
                long rows = 0;
                statement.setLong(1, id);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("menu_item table delete failed, no rows affected");
                }
            }
            String menuSql = "DELETE FROM  pizza_manager.menu\n" +
                    "WHERE pizza_manager.menu.id=?;";
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
            String sql = "SELECT id FROM pizza_manager.menu\n WHERE id=?\n ORDER BY id;";
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
            String sql = "SELECT name FROM pizza_manager.pizza_info\n WHERE name=?\n ORDER BY name;";
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
