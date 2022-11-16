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
public class MenuDtoOutput {
    private final @NonNull Long id;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
    private final @NonNull String name;
    private final @NonNull Boolean enable;
    private final @NonNull List<MenuItemDtoOutput> items;
}
