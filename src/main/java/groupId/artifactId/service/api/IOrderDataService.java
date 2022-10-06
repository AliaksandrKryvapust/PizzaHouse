package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IOrderStage;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface IOrderDataService {
    void add(IToken token);
    void addOrderStage(OrderStageDtoWithId orderStageDtoWithId);
    List<IMenu> get();
    Boolean isIdValid(int id);
    Boolean isDishExist(String name);
}
