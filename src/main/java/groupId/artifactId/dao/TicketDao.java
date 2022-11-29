package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.*;

public class TicketDao implements ITicketDao {
    private static final String SELECT_TICKET = "SELECT ticket from Ticket ticket ORDER BY ticket.id";
    private final EntityManager entityManager;

    public TicketDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ITicket save(ITicket ticket, EntityManager entityTransaction) {
        if (ticket.getId() != null) {
            throw new IllegalStateException("Ticket id should be empty");
        }
        try {
            entityTransaction.persist(ticket);
            return ticket;
        } catch (Exception e) {
            if (e.getMessage().contains(TICKET_UK) || e.getMessage().contains(SELECTED_ITEM_FK) ||
                    e.getMessage().contains(SELECTED_ITEM_FK2)) {
                throw new NoContentException("ticket table insert failed,  check preconditions and FK values: "
                        + ticket);
            } else {
                throw new DaoException("Failed to save new Ticket" + ticket + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<ISelectedItem> saveItems(List<ISelectedItem> items, EntityManager entityTransaction) {
        try {
            items.forEach(entityTransaction::persist);
            return items;
        } catch (Exception e) {
            if (e.getMessage().contains(TICKET_UK) || e.getMessage().contains(SELECTED_ITEM_FK) ||
                    e.getMessage().contains(SELECTED_ITEM_FK2)) {
                throw new NoContentException("ticket table insert failed,  check preconditions and FK values: "
                        + items);
            } else {
                throw new DaoException("Failed to save new Ticket" + items + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public IOrder update(IOrder order, List<ISelectedItem> items, EntityManager entityTransaction) {
        try {
            Order update = (Order) order;
            update.setSelectedItems(items);
            entityTransaction.merge(update);
            return update;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            if (e.getMessage().contains(SELECTED_ITEM_FK) || e.getMessage().contains(SELECTED_ITEM_FK2)) {
                throw new NoContentException("selected_item table insert failed,  check preconditions and FK values: "
                        + items);
            } else {
                throw new DaoException("Failed to update selected_item" + items + " at order:" + order + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<ITicket> get() {
        try {
            List<?> iTicket = entityManager.createQuery(SELECT_TICKET).getResultList();
            List<ITicket> output = iTicket.stream().filter((i) -> i instanceof ITicket)
                    .map(ITicket.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of tickets");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of tickets\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public ITicket get(Long id) {
        try {
            Ticket ticket = entityManager.find(Ticket.class, id);
            if (ticket == null) {
                throw new NoContentException("There is no Ticket with id:" + id);
            }
            return ticket;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Ticket from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id, Boolean delete, EntityManager entityTransaction) {
        try {
            ITicket ticket = this.getLock(id, entityTransaction);
            if (delete) {
                entityTransaction.remove(ticket);
            }
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to delete Ticket with id:" + id + "\tcause: " + e.getMessage(), e);
        }
    }

    private ITicket getLock(Long id, EntityManager entityTransaction) {
        try {
            ITicket ticket = entityTransaction.find(Ticket.class, id);
            if (ticket == null) {
                throw new NoContentException("There is no Ticket with id:" + id);
            }
            return ticket;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Lock of Ticket from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }
}
