package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenuItem;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMenuItemDao extends IDao<IMenuItem>, IDaoUpdate<IMenuItem>, IDaoDelete {
    IMenuItem getLock(Long id, EntityManager entityTransaction);
    List<IMenuItem> getAllLock(List<Long> ids,EntityManager entityTransaction);
}
