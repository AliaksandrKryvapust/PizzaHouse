package groupId.artifactId.core.dto.input;

import groupId.artifactId.storage.entity.Token;
import groupId.artifactId.storage.entity.api.IOrderStage;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.ArrayList;
import java.util.List;

public class OrderDataDto {
    private Token token;
    private List<IOrderStage> orderHistory = new ArrayList<>();
    private Boolean done;

    public OrderDataDto() {
    }

    public OrderDataDto(Token token, Boolean done) {
        this.token = token;
        this.done = done;
    }

    public OrderDataDto(Token token, List<IOrderStage> orderHistory, Boolean done) {
        this.token = token;
        this.orderHistory = orderHistory;
        this.done = done;
    }

    public IToken getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<IOrderStage> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<IOrderStage> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
