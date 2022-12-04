package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.COMPLETED_ORDER_FK;
import static groupId.artifactId.core.Constants.PIZZA_FK;

@Repository
public class CompletedOrderDao implements ICompletedOrderDao {
    private static final String SELECT_COMPLETED_ORDER = "SELECT order from CompletedOrder order ORDER BY order.id";
    private static final String SELECT_COMPLETED_ORDER_BY_TICKET = "SELECT order from CompletedOrder order WHERE ticket.id=?1";
    @PersistenceContext
    private final EntityManager entityManager;

    public CompletedOrderDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ICompletedOrder save(ICompletedOrder completedOrder, EntityManager entityTransaction) {
        if (completedOrder.getId() != null) {
            throw new IllegalStateException("Completed order id should be empty");
        }
        try {
            entityTransaction.persist(completedOrder);
            return completedOrder;
        } catch (PersistenceException e) {
            if (e.getMessage().contains(COMPLETED_ORDER_FK) || e.getMessage().contains(PIZZA_FK)) {
                throw new NoContentException("completed order table insert failed,  check preconditions and FK values: "
                        + completedOrder);
            } else {
                throw new DaoException("Failed to save new Order data" + completedOrder + "\t cause" + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to save new Order data" + completedOrder + "\t cause" + e.getMessage(), e);

        }
    }

    @Override
    public List<ICompletedOrder> get() {
        try {
            List<?> iCompletedOrder = entityManager.createQuery(SELECT_COMPLETED_ORDER).getResultList();
            List<ICompletedOrder> output = iCompletedOrder.stream().filter((i) -> i instanceof ICompletedOrder)
                    .map(ICompletedOrder.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of completed orders");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of completed orders\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public ICompletedOrder get(Long id) {
        try {
            List<?> iCompletedOrder = entityManager.createQuery(SELECT_COMPLETED_ORDER_BY_TICKET).setParameter(1, id)
                    .getResultList();
            ICompletedOrder completedOrder = iCompletedOrder.stream().filter((i) -> i instanceof ICompletedOrder)
                    .map(ICompletedOrder.class::cast).findFirst().orElse(null);
            if (completedOrder == null) {
                throw new NoContentException("There is no Completed Order with ticket id:" + id);
            }
            return completedOrder;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Ticket from Completed Order by id:" + id + "cause: " + e.getMessage(), e);
        }
    }
}


