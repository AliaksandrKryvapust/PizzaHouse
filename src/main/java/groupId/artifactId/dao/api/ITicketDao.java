package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITicketDao extends IDao<ITicket>, IDaoDelete {
    List<ISelectedItem> saveItems(List<ISelectedItem> items, EntityManager entityTransaction);
    IOrder update(IOrder order, List<ISelectedItem> items, EntityManager entityTransaction);
}
