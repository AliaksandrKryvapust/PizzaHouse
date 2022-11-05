package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.ITicket;

public interface ITicketDao extends IDao<ITicket>, IDaoDelete {
    ITicket getAllData(Long id);

    Boolean exist(Long id);
}
