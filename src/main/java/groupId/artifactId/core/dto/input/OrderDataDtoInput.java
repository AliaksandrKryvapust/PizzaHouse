package groupId.artifactId.core.dto.input;

import groupId.artifactId.dao.entity.api.ITicket;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@Jacksonized
public class OrderDataDtoInput {
    private final @NonNull Long ticketId;
    private final @NonNull String description;
    private final ITicket ticket;
}
