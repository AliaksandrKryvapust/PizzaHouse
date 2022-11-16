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
public class SelectedItemDtoInput {
    private final @NonNull Long menuItemId;
    private final @NonNull Integer count;
}
