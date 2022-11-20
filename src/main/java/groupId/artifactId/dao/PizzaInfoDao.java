package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.PIZZA_INFO_UK;

public class PizzaInfoDao implements IPizzaInfoDao {
    //    private final DataSource dataSource;
    private final EntityManager entityManager;
    private static final String INSERT_PIZZA_INFO_SQL = "INSERT INTO pizza_manager.pizza_info (name, description, size) " +
            "VALUES (?, ?, ?);";
    private static final String UPDATE_PIZZA_INFO_SQL = "UPDATE pizza_manager.pizza_info SET name=?, description=?, size=?, " +
            "version=version+1 WHERE id=? AND version=?;";
    private static final String SELECT_PIZZA_INFO_SQL = "SELECT id, name, description, size, creation_date, version " +
            "FROM pizza_manager.pizza_info ORDER BY id";
    private static final String SELECT_PIZZA_INFO = "SELECT pizzaInfo from PizzaInfo pizzaInfo ORDER BY id";
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

    @Override
    public IPizzaInfo save(IPizzaInfo info) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try {
            entityManager.persist(info);
            return info;
        } catch (Exception e) {
            if (e.getMessage().contains(PIZZA_INFO_UK)) {
                throw new NoContentException("pizza_info table insert failed,  check preconditions and FK values: "
                        + info);
            } else {
                throw new DaoException("Failed to save new PizzaInfo" + info + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public IPizzaInfo update(IPizzaInfo info, Long id, Integer version) {
        if (info.getId() != null || info.getVersion() != null) {
            throw new IllegalStateException("PizzaInfo id & version should be empty");
        }
        try {
            PizzaInfo currentEntity = (PizzaInfo) this.getLock(id);
//            entityManager.lock(currentEntity, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            entityManager.detach(currentEntity);
            if (!currentEntity.getVersion().equals(version)) {
                throw new OptimisticLockException();
            }
            currentEntity.setName(info.getName());
            currentEntity.setDescription(info.getDescription());
            currentEntity.setSize(info.getSize());
            entityManager.merge(currentEntity);
            return currentEntity;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("pizza_info table update failed, version does not match update denied");
        } catch (Exception e) {
            throw new DaoException("Failed to update pizza_info" + info + " by id:" + id + "\t cause" + e.getMessage(), e);
        }
    }

    @Override
    public IPizzaInfo get(Long id) {
        try {
            PizzaInfo pizzaInfo = entityManager.find(PizzaInfo.class, id);
            if (pizzaInfo == null) {
                throw new NoContentException("There is no Pizza Info with id:" + id);
            }
            return pizzaInfo;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Pizza Info from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id, Boolean delete) {
        try {
            PizzaInfo pizzaInfo = entityManager.find(PizzaInfo.class, id);
            if (pizzaInfo == null) {
                throw new NoContentException("There is no Pizza Info with id:" + id);
            }
            entityManager.remove(pizzaInfo);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to delete pizza info with id:" + id + "\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IPizzaInfo> get() {
        try {
            List<?> iPizzaInfos = entityManager.createQuery(SELECT_PIZZA_INFO).getResultList();
            List<IPizzaInfo> output = iPizzaInfos.stream().filter((i) -> i instanceof IPizzaInfo)
                    .map(IPizzaInfo.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of pizza Info");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of pizza Info\tcause: " + e.getMessage(), e);
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

    private IPizzaInfo getLock(Long id) {
        try {
            PizzaInfo pizzaInfo = entityManager.find(PizzaInfo.class, id, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (pizzaInfo == null) {
                throw new NoContentException("There is no Pizza Info with id:" + id);
            }
            return pizzaInfo;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Lock of Pizza Info from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    private IPizzaInfo mapper(ResultSet resultSet) throws SQLException {
        return new PizzaInfo(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getInt("size"),
                resultSet.getTimestamp("creation_date").toInstant(), resultSet.getInt("version"));
    }
}
