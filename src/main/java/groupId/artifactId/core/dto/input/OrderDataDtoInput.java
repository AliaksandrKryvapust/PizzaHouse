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
public class OrderDataDtoInput {
    private final @NonNull Long ticketId;
    private final @NonNull Boolean done;
    private final @NonNull String description;
}
