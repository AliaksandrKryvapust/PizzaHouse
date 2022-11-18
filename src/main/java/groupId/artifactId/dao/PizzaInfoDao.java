package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.OptimisticLockException;

import jakarta.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PizzaInfoDao implements IPizzaInfoDao {
    //    private final DataSource dataSource;
    private final EntityManager entityManager;
    private static final String INSERT_PIZZA_INFO_SQL = "INSERT INTO pizza_manager.pizza_info (name, description, size) " +
            "VALUES (?, ?, ?);";
    private static final String UPDATE_PIZZA_INFO_SQL = "UPDATE pizza_manager.pizza_info SET name=?, description=?, size=?, " +
            "version=version+1 WHERE id=? AND version=?;";
    private static final String SELECT_PIZZA_INFO_SQL = "SELECT id, name, description, size, creation_date, version " +
            "FROM pizza_manager.pizza_info ORDER BY id";
    private static final String SELECT_PIZZA_INFO = "SELECT pizzaInfo from PizzaInfo pizzaInfo";
    private static final String SELECT_PIZZA_INFO_BY_ID_SQL = "SELECT id, name, description, size, creation_date, " +
            "version FROM pizza_manager.pizza_info WHERE id=?;";
    private static final String SELECT_PIZZA_INFO_BY_NAME_SQL = "SELECT name FROM pizza_manager.pizza_info WHERE name=?;";
    private static final String DELETE_PIZZA_INFO_SQL = "DELETE FROM pizza_manager.pizza_info WHERE id=?;";

    //    public PizzaInfoDao(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
    public PizzaInfoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //    @Override
//    public IPizzaInfo save(IPizzaInfo info) {
//        if (info.getId() != null || info.getVersion() != null) {
//            throw new IllegalStateException("PizzaInfo id & version should be empty");
//        }
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(INSERT_PIZZA_INFO_SQL, Statement.RETURN_GENERATED_KEYS)) {
//                long rows = 0;
//                statement.setString(1, info.getName());
//                statement.setString(2, info.getDescription());
//                statement.setLong(3, info.getSize());
//                rows += statement.executeUpdate();
//                if (rows > 1) {
//                    throw new IllegalStateException("Incorrect pizza_info table update, more than 1 row affected");
//                }
//                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                    generatedKeys.next();
//                    return new PizzaInfo(generatedKeys.getLong(1), info.getName(),
//                            info.getDescription(), info.getSize());
//                }
//            }
//        } catch (SQLException e) {
//            if (e.getMessage().contains("pizza_info_pkey")) {
//                throw new NoContentException("pizza_info table insert failed,  check preconditions and FK values: "
//                        + info);
//            } else {
//                throw new DaoException("Failed to save new PizzaInfo" + info, e);
//            }
//        }
//    }
    @Override
    public IPizzaInfo save(IPizzaInfo info) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(info);
            entityManager.getTransaction().commit();
            return info;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to save new PizzaInfo" + info, e);
        }
    }

//    @Override
//    public IPizzaInfo update(IPizzaInfo info, Long id, Integer version) {
//        if (info.getId() != null || info.getVersion() != null) {
//            throw new IllegalStateException("PizzaInfo id & version should be empty");
//        }
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(UPDATE_PIZZA_INFO_SQL)) {
//                long rows = 0;
//                statement.setString(1, info.getName());
//                statement.setString(2, info.getDescription());
//                statement.setLong(3, info.getSize());
//                statement.setLong(4, id);
//                statement.setInt(5, version);
//                rows += statement.executeUpdate();
//                if (rows == 0) {
//                    throw new OptimisticLockException("pizza_info table update failed, version does not match update denied");
//                }
//                if (rows > 1) {
//                    throw new IllegalStateException("Incorrect pizza_info table update, more than 1 row affected");
//                }
//                return new PizzaInfo(id, info.getName(), info.getDescription(), info.getSize());
//            }
//        } catch (SQLException e) {
//            throw new DaoException("Failed to update pizza_info" + info + " by id:" + id, e);
//        }
//    }

    @Override
    public IPizzaInfo update(IPizzaInfo info, Long id, Integer version) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try {
            IPizzaInfo pizzaInfo = this.get(id);
            entityManager.detach(pizzaInfo);
            if (!pizzaInfo.getVersion().equals(version)) {
                throw new OptimisticLockException("pizza_info table update failed, version does not match update denied");
            }
            PizzaInfo pizzaInfoUpdate = new PizzaInfo(pizzaInfo.getId(), info.getName(), info.getDescription(),
                    info.getSize(), pizzaInfo.getCreationDate(), pizzaInfo.getVersion());
            entityManager.getTransaction().begin();
            entityManager.merge(pizzaInfoUpdate);
            entityManager.getTransaction().commit();
            return pizzaInfoUpdate;
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to update pizza_info" + info + " by id:" + id, e);
        }
    }

//    @Override
//    public IPizzaInfo get(Long id) {
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_ID_SQL)) {
//                statement.setLong(1, id);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    resultSet.next();
//                    if (!resultSet.isLast()) {
//                        throw new NoContentException("There is no Pizza Info with id:" + id);
//                    }
//                    return this.mapper(resultSet);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DaoException("Failed to get Pizza Info by id:" + id, e);
//        }
//    }

    @Override
    public IPizzaInfo get(Long id) {
        try {
            PizzaInfo pizzaInfo = entityManager.find(PizzaInfo.class, id);
            entityManager.detach(pizzaInfo);
            return pizzaInfo;
        } catch (Exception e) {
            throw new DaoException("Failed to get Pizza Info by id:" + id, e);
        }
    }

//    @Override
//    public void delete(Long id, Boolean delete) {
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(DELETE_PIZZA_INFO_SQL)) {
//                long rows = 0;
//                statement.setLong(1, id);
//                rows += statement.executeUpdate();
//                if (rows > 1) {
//                    throw new IllegalStateException("Incorrect pizza_info table delete, more than 1 row affected");
//                }
//            }
//        } catch (Exception e) {
//            throw new DaoException("Failed to delete pizza info with id:" + id, e);
//        }
//    }

    @Override
    public void delete(Long id, Boolean delete) {
        try {
            entityManager.getTransaction().begin();
            PizzaInfo pizzaInfo = entityManager.find(PizzaInfo.class, id);
            entityManager.remove(pizzaInfo);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new DaoException("Failed to delete pizza info with id:" + id, e);
        }
    }

//    @Override
//    public List<IPizzaInfo> get() {
//        try (Connection con = dataSource.getConnection()) {
//            List<IPizzaInfo> iPizzaInfos = new ArrayList<>();
//            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_SQL)) {
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    while (resultSet.next()) {
//                        iPizzaInfos.add(this.mapper(resultSet));
//                    }
//                }
//                return iPizzaInfos;
//            }
//        } catch (Exception e) {
//            throw new DaoException("Failed to get List of pizza Info", e);
//        }
//    }

    @Override
    public List<IPizzaInfo> get() {
        try {
            List<?> iPizzaInfos = entityManager.createQuery(SELECT_PIZZA_INFO).getResultList();
            if (iPizzaInfos instanceof IPizzaInfo) {
                return (List<IPizzaInfo>) iPizzaInfos;
            } else {
                throw new IllegalStateException("Failed to get List of pizza Info");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of pizza Info", e);
        }
    }
    @Override
    public Boolean exist(Long id) {
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_ID_SQL)) {
//                statement.setLong(1, id);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    return resultSet.next();
//                }
//            }
//        } catch (Exception e) {
//            throw new DaoException("Failed to select Pizza Info with id:" + id, e);
//        }
        return true;
    }

    @Override
    public Boolean doesPizzaExist(String name) {
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement statement = con.prepareStatement(SELECT_PIZZA_INFO_BY_NAME_SQL)) {
//                statement.setString(1, name);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    return resultSet.next();
//                }
//            }
//        } catch (Exception e) {
//            throw new DaoException("Failed to select Pizza Info with name:" + name, e);
//        }
        return true;
    }

    private IPizzaInfo mapper(ResultSet resultSet) throws SQLException {
        return new PizzaInfo(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getInt("size"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }
}
