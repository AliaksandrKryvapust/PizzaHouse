package groupId.artifactId.entity.api;

import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface IOrderData {
    IToken getToken();

    List<IOrderStage> getHistory();

    boolean isDone();
}
