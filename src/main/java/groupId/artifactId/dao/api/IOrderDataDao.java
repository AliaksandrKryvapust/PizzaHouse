package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrderData;

import javax.persistence.EntityManager;

public interface IOrderDataDao extends IDao<IOrderData> {
    IOrderData update(IOrderData orderData, EntityManager entityTransaction);
}
