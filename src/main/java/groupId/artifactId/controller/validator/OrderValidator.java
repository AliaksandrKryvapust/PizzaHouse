package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IOrderValidator;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;

public class OrderValidator implements IOrderValidator {

    @Override
    public void validate(OrderDtoInput orderDtoInput) {
        for (SelectedItemDtoInput dto : orderDtoInput.getSelectedItems()) {
            if (dto.getMenuItemId() <= 0) {
                throw new IllegalArgumentException("Menu item id in Order is not valid");
            }
            if (dto.getCount() <= 0) {
                throw new IllegalArgumentException("Menu item count in Order is not valid");
            }
        }
    }
}
