package groupId.artifactId.core.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class TicketDtoOutput {
    private final @NonNull OrderDtoOutput order;
    private final @NonNull Long id;
    private final @NonNull Instant createdAt;
}
