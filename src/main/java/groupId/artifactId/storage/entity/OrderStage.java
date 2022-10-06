package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IOrderStage;

import java.time.LocalTime;

public class OrderStage implements IOrderStage {
    private String description;
    private LocalTime time;

    public OrderStage(String description, LocalTime time) {
        this.description = description;
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }
}
