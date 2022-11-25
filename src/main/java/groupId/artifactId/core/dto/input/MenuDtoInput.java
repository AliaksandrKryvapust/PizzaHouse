package groupId.artifactId.core.dto.input;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@ToString
@Jacksonized
public class MenuDtoInput {
    private final @NonNull String name;
    private final @NonNull Boolean enable;
    private final List<MenuItemDtoInput> items;
}
