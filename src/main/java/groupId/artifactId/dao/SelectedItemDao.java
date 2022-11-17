package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ISelectedItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.SELECTED_ITEM_FK;
import static groupId.artifactId.core.Constants.SELECTED_ITEM_FK2;

public class SelectedItemDao implements ISelectedItemDao {
    private final DataSource dataSource;
    private static final String INSERT_SELECTED_ITEM_SQL = "INSERT INTO pizza_manager.selected_item (menu_item_id, order_id, count) " +
            "VALUES (?, ?, ?);";
    private static final String SELECT_SELECTED_ITEM_SQL = "SELECT id, menu_item_id, order_id, count,  creation_date, version " +
            "FROM pizza_manager.selected_item ORDER BY id;";
    private static final String SELECT_SELECTED_ITEM_BY_ID_SQL = "SELECT id, menu_item_id, order_id, count," +
            " creation_date, version FROM pizza_manager.selected_item WHERE id=?;";
    private static final String SELECT_SELECTED_ITEM_ALL_DATA_SQL = "SELECT si.id AS siid, menu_item_id, order_id, count ," +
            "si.creation_date AS sicd, si.version AS siiv, mi.id AS miid, price, pizza_info_id, mi.creation_date AS micd, " +
            "mi.version AS miver, mi.menu_id AS meid, name, description, size, " +
            "pi.creation_date AS picd, pi.version AS piv FROM pizza_manager.selected_item si " +
            " INNER JOIN menu_item mi on mi.id = si.menu_item_id" +
            " JOIN pizza_info pi on pi.id = mi.pizza_info_id WHERE si.id=? ORDER BY siid, miid, pizza_info_id;";
    private static final String DELETE_SELECTED_ITEM_SQL = "DELETE FROM pizza_manager.selected_item WHERE order_id=? AND version=?;";

    public SelectedItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ISelectedItem save(ISelectedItem selectedItem) {
        if (selectedItem.getId() != null || selectedItem.getVersion() != null) {
            throw new IllegalStateException("Selected Item id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_SELECTED_ITEM_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setLong(1, selectedItem.getMenuItemId());
                statement.setLong(2, selectedItem.getOrderId());
                statement.setInt(3, selectedItem.getCount());
                rows += statement.executeUpdate();
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect selected item table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new SelectedItem(generatedKeys.getLong(1), selectedItem.getMenuItemId(),
                            selectedItem.getOrderId(), selectedItem.getCount());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains(SELECTED_ITEM_FK) || e.getMessage().contains(SELECTED_ITEM_FK2)) {
                throw new NoContentException("selected item table insert failed, check preconditions and FK values: "
                        + selectedItem);
            } else {
                throw new DaoException("Failed to save new Selected item" + selectedItem, e);
            }
        }
    }

    @Override
    public List<ISelectedItem> get() {
        try (Connection con = dataSource.getConnection()) {
            List<ISelectedItem> items = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_SELECTED_ITEM_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        items.add(this.mapper(resultSet));
                    }
                }
                return items;
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of selected Items", e);
        }
    }

    @Override
    public ISelectedItem get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_SELECTED_ITEM_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    if (!resultSet.isLast()) {
                        throw new NoContentException("There is no Selected Item with id:" + id);
                    }
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Selected Item by id:" + id, e);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_SELECTED_ITEM_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new OptimisticLockException("selected item table delete failed,version does not match update denied");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete selected item with id:" + id, e);
        }
    }

    @Override
    public ISelectedItem getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_SELECTED_ITEM_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.allDataMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Selected items with menu items by id:" + id, e);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_SELECTED_ITEM_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (Exception e) {
            throw new DaoException("Failed to select selected item with id:" + id, e);
        }
    }

    private ISelectedItem mapper(ResultSet resultSet) throws SQLException {
        return new SelectedItem(resultSet.getLong("id"), resultSet.getLong("menu_item_id"),
                resultSet.getLong("order_id"), resultSet.getInt("count"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }

    private ISelectedItem allDataMapper(ResultSet resultSet) throws SQLException {
        IPizzaInfo pizzaInfo = new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getInt("size"),
                resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piv"));
        IMenuItem menuItem = new MenuItem(resultSet.getLong("miid"), pizzaInfo, resultSet.getDouble("price"),
                resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("micd").toInstant(),
                resultSet.getInt("miver"), resultSet.getLong("meid"));
        ISelectedItem selectedItem = new SelectedItem(menuItem, resultSet.getLong("siid"),
                resultSet.getLong("menu_item_id"), resultSet.getLong("order_id"), resultSet.getInt("count"),
                resultSet.getTimestamp("sicd").toInstant(), resultSet.getInt("siiv"));
        if (selectedItem.getItem() == null || selectedItem.getId() == null) {
            throw new NoContentException("There is no Selected Item with such id");
        }
        return selectedItem;
    }
}
