package groupId.artifactId.core.dto.output.crud;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class MenuDtoCrudOutput {
    private final @NonNull Long id;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
    private final @NonNull String name;
    private final @NonNull Boolean enable;
}
