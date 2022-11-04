package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrder;

public interface IOrderDao extends IDao<IOrder>{
    Boolean exist(Long id);
}
