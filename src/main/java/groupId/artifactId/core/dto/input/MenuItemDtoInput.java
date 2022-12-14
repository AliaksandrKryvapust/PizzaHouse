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
public class MenuItemDtoInput {
    private final @NonNull Double price;
    private final @NonNull PizzaInfoDtoInput pizzaInfoDtoInput;
    private final Long menuId;
}
