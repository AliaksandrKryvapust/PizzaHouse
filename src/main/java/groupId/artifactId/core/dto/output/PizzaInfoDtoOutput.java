package groupId.artifactId.core.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class PizzaInfoDtoOutput {
    private final @NonNull Long id;
    private final @NonNull String name;
    private final @NonNull String description;
    private final @NonNull Integer size;
    private final Instant createdAt;
    private final Integer version;
}
