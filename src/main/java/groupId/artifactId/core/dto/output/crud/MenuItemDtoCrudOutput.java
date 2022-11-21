package groupId.artifactId.core.dto.output.crud;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@ToString
public class MenuItemDtoCrudOutput {
    private final @NonNull Long id;
    private final @NonNull Double price;
    private final @NonNull Long pizzaInfoId;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
    private final @NonNull Long menuId;
}
