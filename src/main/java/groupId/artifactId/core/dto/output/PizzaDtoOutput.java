package groupId.artifactId.core.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class PizzaDtoOutput {
    private final @NonNull Long id;
    private final @NonNull Long completedOrderId;
    private final @NonNull String name;
    private final @NonNull Integer size;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
}
