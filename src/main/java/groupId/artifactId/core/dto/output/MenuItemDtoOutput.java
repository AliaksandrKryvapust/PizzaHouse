package groupId.artifactId.core.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class MenuItemDtoOutput {
    private final @NonNull Long id;
    private final @NonNull Double price;
    private final @NonNull Instant createdAt;
    private final @NonNull Integer version;
    private final @NonNull Long menuId;
    private final @NonNull PizzaInfoDtoOutput pizzaInfo;
}
