package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrder;

public interface IOrderDao extends IDao<IOrder>, IDaoDelete{
    IOrder getAllData(Long id);
    Boolean exist(Long id);
}
