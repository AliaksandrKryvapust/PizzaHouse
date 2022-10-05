package groupId.artifactId.entity.api;

import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface ICompletedOrder {
    IToken getToken();

    List<IPizza> getItems();
}
