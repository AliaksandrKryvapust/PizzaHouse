package groupId.artifactId.entity;

import groupId.artifactId.entity.api.IOrder;
import groupId.artifactId.entity.api.IToken;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Token implements IToken {
    private AtomicInteger id;
    private LocalDateTime createAt;
    private IOrder order;

    public Token(AtomicInteger id, LocalDateTime createAt, IOrder order) {
        this.id = id;
        this.createAt = createAt;
        this.order = order;
    }

    public void setId(AtomicInteger id) {
        this.id = id;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public AtomicInteger getId() {
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
