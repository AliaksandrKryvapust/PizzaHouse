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
    public void save(IPizzaInfo iPizzaInfo) throws SQLException {

    }

    @Override
    public void update(IPizzaInfo iPizzaInfo) throws SQLException {

    }

    @Override
    public IPizzaInfo get(Long id) throws SQLException {
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
    public void delete(Long id, Integer version) throws SQLException {

    }

    @Override
    public List<PizzaInfo> get() {
        try (Connection con = dataSource.getConnection()) {
            List<PizzaInfo> infos = new ArrayList<>();
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
                        infos.add(temp);
                    }
                }
                return infos;
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
