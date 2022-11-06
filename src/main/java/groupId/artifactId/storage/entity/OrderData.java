package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IOrderStage;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public class OrderData implements IOrderData {
    private IToken token;
    private List<IOrderStage> orderHistory;
    private Boolean done;

    public OrderData(IToken token, Boolean done) {
        this.token = token;
        this.done = done;
    }

    public OrderData(IToken token, List<IOrderStage> orderHistory, Boolean done) {
        this.token = token;
        this.orderHistory = orderHistory;
        this.done = done;
    }

    public void setToken(IToken token) {
        this.token = token;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public IToken getToken() {
        return token;
    }

    @Override
    public List<IOrderStage> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<IOrderStage> orderHistory) {
        this.orderHistory = orderHistory;
    }

    @Override
    public Boolean isDone() {
        return done;
    }
}
