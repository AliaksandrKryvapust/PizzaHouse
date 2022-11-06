package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrderData;

public interface IOrderDataDao extends IDao<IOrderData>, IDaoUpdate<IOrderData> {
    IOrderData getAllData(Long id);
    IOrderData getDataByTicket(Long id);

    Boolean exist(Long id);
    Boolean doesTicketExist(Long id);
}
