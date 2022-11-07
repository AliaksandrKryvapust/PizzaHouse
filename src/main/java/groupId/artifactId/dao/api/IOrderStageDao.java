package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IOrderStage;

public interface IOrderStageDao extends IDao<IOrderStage> {

    Boolean exist(Long id);

    Boolean doesStageExist(Long orderDataId, String name);
}
