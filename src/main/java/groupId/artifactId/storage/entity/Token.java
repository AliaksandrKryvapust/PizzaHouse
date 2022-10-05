package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IOrder;
import groupId.artifactId.storage.entity.api.IToken;

import java.time.LocalDateTime;

public class Token implements IToken {
    private Integer id;
    private LocalDateTime createAt;
    private IOrder order;

    public Token() {
    }

    public Token(IOrder order) {
        this.order = order;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public LocalDateTime getCreatAt() {
        return createAt;
    }

    @Override
    public IOrder getOrder() {
        return order;
    }
}
