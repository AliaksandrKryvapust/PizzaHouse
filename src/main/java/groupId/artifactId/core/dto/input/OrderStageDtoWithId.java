package groupId.artifactId.core.dto.input;

import java.time.LocalTime;

public class OrderStageDtoWithId {
    private String description;
    private LocalTime time;
    Integer id;

    public OrderStageDtoWithId() {
    }

    public OrderStageDtoWithId(String description, LocalTime time, Integer id) {
        this.description = description;
        this.time = time;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
