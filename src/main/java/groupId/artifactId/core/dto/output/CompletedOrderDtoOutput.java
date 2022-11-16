package groupId.artifactId.core.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@ToString
public class CompletedOrderDtoOutput {
    private final @NonNull TicketDtoOutput ticket;
    private final @NonNull List<PizzaDtoOutput> items;
    private final @NonNull Long id;
    private final @NonNull Long ticketId;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
}
