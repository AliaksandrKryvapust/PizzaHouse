package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PizzaInfoDao implements IPizzaInfoDao {
    private static PizzaInfoDao firstInstance = null;
    private final DataSource dataSource;

    public PizzaInfoDao() {
        try {
            this.dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class at PizzaInfoDao", e);
        }
    }

    public static PizzaInfoDao getInstance() {
        synchronized (PizzaInfoDao.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoDao();
            }
        }
        return firstInstance;
    }

    @Override
    public void save(IPizzaInfo iPizzaInfo) {
        PizzaInfo info = (PizzaInfo) iPizzaInfo;
        if (info.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSql = "INSERT INTO pizza_manager.pizza_info (name, description, size)\n VALUES (?, ?, ?);";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSql)) {
                long rows = 0;
                statement.setString(1, info.getName());
                statement.setString(2, info.getDescription());
                statement.setLong(3, info.getSize());
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table insert failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IPizzaInfo iPizzaInfo, Long id, Integer version) {
        PizzaInfo pizzaInfo = (PizzaInfo) iPizzaInfo;
        if (this.isIdExist(pizzaInfo.getId())) {
            try (Connection con = dataSource.getConnection()) {
                String pizzaInfoSqlUpdate = "UPDATE pizza_manager.pizza_info\n " +
                        "SET name=?, description=?, size=?, version=version+1\n" +
                        "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
                try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlUpdate)) {
                    long rows = 0;
                    statement.setString(1, pizzaInfo.getName());
                    statement.setString(2, pizzaInfo.getDescription());
                    statement.setLong(3, pizzaInfo.getSize());
                    statement.setLong(4, pizzaInfo.getId());
                    statement.setInt(5, pizzaInfo.getVersion());
                    rows += statement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("pizza_info table update failed, no rows affected");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalStateException("Error code 500. PizzaInfo id is not valid");
    }

    @Override
    public IPizzaInfo get(Long id) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. PizzaInfo id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            PizzaInfo info = new PizzaInfo();
            info.setId(id);
            String sql = "SELECT pizza_info.id AS id, name, description, size, pizza_info.creation_date AS picd, pizza_info.version AS pied\n " +
                    "FROM pizza_manager.pizza_info\n" +
                    "WHERE pizza_manager.pizza_info.id=?\n ORDER BY id;";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long itemId = resultSet.getLong("id");
                        if (!resultSet.wasNull()) {
                            info.setId(itemId);
                        }
                        info.setName(resultSet.getString("name"));
                        info.setDescription(resultSet.getString("description"));
                        long size = resultSet.getLong("size");
                        if (!resultSet.wasNull()) {
                            info.setSize(size);
                        }
                        info.setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                        info.setVersion(resultSet.getInt("pied"));
                    }
                    return info;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id, Integer version) {
        if (!this.isIdExist(id)) {
            throw new IllegalStateException("Error code 500. MenuItem id is not valid");
        }
        try (Connection con = dataSource.getConnection()) {
            String pizzaInfoSqlDelete = "DELETE FROM pizza_manager.pizza_info\n " +
                    "WHERE pizza_manager.pizza_info.id=? AND pizza_manager.pizza_info.version=?\n";
            try (PreparedStatement statement = con.prepareStatement(pizzaInfoSqlDelete)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("pizza_info table delete failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IPizzaInfo> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IPizzaInfo> iPizzaInfos = new ArrayList<>();
            String sqlSelect = "SELECT pizza_info.id AS id, name, description, size, pizza_info.creation_date AS picd, pizza_info.version  AS pied\n" +
                    "FROM pizza_manager.pizza_info\n" +
                    "ORDER BY id;";
            try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        PizzaInfo temp = new PizzaInfo();
                        long id = resultSet.getLong("id");
                        if (!resultSet.wasNull()) {
                            temp.setId(id);
                        }
                        temp.setName(resultSet.getString("name"));
                        temp.setDescription(resultSet.getString("description"));
                        long size = resultSet.getLong("size");
                        if (!resultSet.wasNull()) {
                            temp.setSize(size);
                        }
                        temp.setCreationDate(resultSet.getTimestamp("picd").toLocalDateTime());
                        temp.setVersion(resultSet.getInt("pied"));
                        iPizzaInfos.add(temp);
                    }
                }
                return iPizzaInfos;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean isIdExist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT id FROM pizza_manager.pizza_info\n WHERE id=?\n ORDER BY id;";
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
}
