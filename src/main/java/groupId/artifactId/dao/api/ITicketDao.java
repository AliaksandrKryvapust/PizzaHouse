package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.ITicket;

public interface ITicketDao extends IDao<ITicket> {
    ITicket getAllData(Long id);

    Boolean exist(Long id);
}
