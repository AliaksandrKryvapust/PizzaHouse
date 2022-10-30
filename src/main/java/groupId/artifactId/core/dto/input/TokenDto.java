package groupId.artifactId.core.dto.input;

import groupId.artifactId.storage.entity.api.IOrder;

import java.time.LocalDateTime;

public class TokenDto {
    private Integer id;

    private LocalDateTime createAt;

    private IOrder order;

    public TokenDto() {
    }

    public TokenDto(Integer id) {
        this.id = id;
    }

    public TokenDto(Integer id, LocalDateTime createAt, IOrder order) {
        this.id = id;
        this.createAt = createAt;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public IOrder getOrder() {
        return order;
    }

    public void setOrder(IOrder order) {
        this.order = order;
    }
}
