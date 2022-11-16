package groupId.artifactId.core.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@ToString
@Jacksonized
public class OrderDtoInput {
    private final @NonNull List<SelectedItemDtoInput> selectedItems;
}
