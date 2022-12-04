package groupId.artifactId.dao;

import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.*;

@Repository
public class TicketDao implements ITicketDao {
    private static final String SELECT_TICKET = "SELECT ticket from Ticket ticket ORDER BY ticket.id";
    @PersistenceContext
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
        } catch (PersistenceException e) {
            if (e.getMessage().contains(TICKET_UK) || e.getMessage().contains(SELECTED_ITEM_FK) ||
                    e.getMessage().contains(SELECTED_ITEM_FK2)) {
                throw new NoContentException("ticket table insert failed,  check preconditions and FK values: "
                        + ticket);
            } else {
                throw new DaoException("Failed to save new Ticket" + ticket + "\t cause" + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to save new Ticket" + ticket + "\t cause" + e.getMessage(), e);
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
}
