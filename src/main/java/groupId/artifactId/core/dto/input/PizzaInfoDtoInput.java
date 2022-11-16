package groupId.artifactId.core.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@Jacksonized
public class PizzaInfoDtoInput {
    private final @NonNull String name;
    private final @NonNull String description;
    private final @NonNull Integer size;
}
