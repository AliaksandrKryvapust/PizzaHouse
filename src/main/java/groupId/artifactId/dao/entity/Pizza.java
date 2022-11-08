package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizza;

import java.time.Instant;

public class Pizza implements IPizza {
    private Long id;
    private Long completedOrderId;
    private String name;
    private Integer size;
    private Instant creationDate;
    private Integer version;

    public Pizza() {
    }

    public Pizza(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public Pizza(Long completedOrderId, String name, Integer size) {
        this.completedOrderId = completedOrderId;
        this.name = name;
        this.size = size;
    }

    public Pizza(Long id, Long completedOrderId, String name, Integer size) {
        this.id = id;
        this.completedOrderId = completedOrderId;
        this.name = name;
        this.size = size;
    }

    public Pizza(Long id, Long completedOrderId, String name, Integer size, Instant creationDate, Integer version) {
        this.id = id;
        this.completedOrderId = completedOrderId;
        this.name = name;
        this.size = size;
        this.creationDate = creationDate;
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getCompletedOrderId() {
        return completedOrderId;
    }

    public void setCompletedOrderId(Long completedOrderId) {
        this.completedOrderId = completedOrderId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
