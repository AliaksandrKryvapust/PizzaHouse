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
    private Instant createdAt;
    private Integer version;
    private final @NonNull Long menuId;
}
