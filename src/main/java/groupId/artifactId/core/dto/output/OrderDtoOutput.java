package groupId.artifactId.core.dto.output;

import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class OrderDtoOutput {
    private final @NonNull List<SelectedItemDtoOutput> selectedItems;
    private final @NonNull Long id;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
}
