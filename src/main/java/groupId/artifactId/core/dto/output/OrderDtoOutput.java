package groupId.artifactId.core.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class OrderDtoOutput {
    private final @NonNull List<SelectedItemDtoOutput> selectedItems;
    private final @NonNull Long id;
}
