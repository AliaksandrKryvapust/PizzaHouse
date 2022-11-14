package groupId.artifactId.core.dto.input;

public class OrderDataDtoInput {
    private Long ticketId;
    private Boolean done;
    private String description;

    public OrderDataDtoInput() {
    }

    public OrderDataDtoInput(Long ticketId, Boolean done, String description) {
        this.ticketId = ticketId;
        this.done = done;
        this.description = description;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OrderDataDtoInput{" +
                "ticketId=" + ticketId +
                ", done=" + done +
                ", description='" + description + '\'' +
                '}';
    }
}
