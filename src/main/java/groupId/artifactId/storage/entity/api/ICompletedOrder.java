package groupId.artifactId.storage.entity.api;

import java.util.List;

public interface ICompletedOrder {
    IToken getToken();

    List<IPizza> getItems();
}
