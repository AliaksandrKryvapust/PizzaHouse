package groupId.artifactId.entity;

import groupId.artifactId.entity.api.ICompletedOrder;
import groupId.artifactId.entity.api.IPizza;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public class CompletedOrder implements ICompletedOrder {
    private IToken token;
    private List<IPizza> items;

    public CompletedOrder(IToken token, List<IPizza> items) {
        this.token = token;
        this.items = items;
    }

    public void setToken(IToken token) {
        this.token = token;
    }

    public void setItems(List<IPizza> items) {
        this.items = items;
    }

    @Override
    public IToken getToken() {
        return token;
    }

    @Override
    public List<IPizza> getItems() {
        return items;
    }
}
