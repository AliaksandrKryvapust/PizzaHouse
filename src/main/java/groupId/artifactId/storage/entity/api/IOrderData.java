package groupId.artifactId.storage.entity.api;

import java.util.List;

public interface IOrderData {
    IToken getToken();

    List<IOrderStage> getHistory();
    void addOrderStage(IOrderStage orderStage);

    boolean isDone();
}
