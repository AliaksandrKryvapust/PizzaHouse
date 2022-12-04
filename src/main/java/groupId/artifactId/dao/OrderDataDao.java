package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.*;

@Repository
public class OrderDataDao implements IOrderDataDao {
    private static final String SELECT_ORDER_DATA = "SELECT data from OrderData data ORDER BY data.id";
    private static final String SELECT_ORDER_DATA_BY_TICKET = "SELECT data from OrderData data WHERE ticket.id=?1";
    @PersistenceContext
    private final EntityManager entityManager;

    public OrderDataDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public IOrderData save(IOrderData orderData, EntityManager entityTransaction) {
        if (orderData.getId() != null) {
            throw new IllegalStateException("Order data id should be empty");
        }
        try {
            entityTransaction.persist(orderData);
            return orderData;
        } catch (Exception e) {
            if (e.getMessage().contains(ORDER_STAGE_UK) || e.getMessage().contains(ORDER_DATA_FK) ||
                    e.getMessage().contains(ORDER_STAGE_FK)) {
                throw new NoContentException("order data table insert failed,  check preconditions and FK values: "
                        + orderData);
            } else {
                throw new DaoException("Failed to save new Order data" + orderData + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<IOrderData> get() {
        try {
            List<?> iOrderData = entityManager.createQuery(SELECT_ORDER_DATA).getResultList();
            List<IOrderData> output = iOrderData.stream().filter((i) -> i instanceof IOrderData)
                    .map(IOrderData.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of order data");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of order data\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public IOrderData get(Long id) {
        try {
            List<?> iOrderData = entityManager.createQuery(SELECT_ORDER_DATA_BY_TICKET).setParameter(1, id)
                    .getResultList();
            IOrderData orderData = iOrderData.stream().filter((i) -> i instanceof IOrderData)
                    .map(IOrderData.class::cast).findFirst().orElse(null);
            if (orderData == null) {
                throw new NoContentException("There is no Order data by ticket id:" + id);
            }
            return orderData;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Ticket from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public IOrderData getOptional(Long id, EntityManager entityTransaction) {
        try {
            List<?> iOrderData = entityTransaction.createQuery(SELECT_ORDER_DATA_BY_TICKET).setParameter(1, id)
                    .getResultList();
            return iOrderData.stream().filter((i) -> i instanceof IOrderData).map(IOrderData.class::cast)
                    .findFirst().orElse(null);
        } catch (Exception e) {
            throw new DaoException("Failed to get Ticket from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public IOrderData update(IOrderData orderData, EntityManager entityTransaction) {
        try {
            entityTransaction.persist(orderData);
            return orderData;
        } catch (Exception e) {
            if (e.getMessage().contains(ORDER_STAGE_UK) || e.getMessage().contains(ORDER_DATA_FK) ||
                    e.getMessage().contains(ORDER_STAGE_FK)) {
                throw new NoContentException("order data table update failed,  check preconditions and FK values: "
                        + orderData);
            } else {
                throw new DaoException("Failed to update new Order data" + orderData + "\t cause" + e.getMessage(), e);
            }
        }
    }
}


