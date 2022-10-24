package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.util.List;

public interface IPizzaInfoDao extends IDao<IPizzaInfo> {
    List<PizzaInfo> get();

    Boolean isIdExist(Long id);
}
