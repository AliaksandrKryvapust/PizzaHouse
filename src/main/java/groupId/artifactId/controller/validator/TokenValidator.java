package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.OrderDto;
import groupId.artifactId.core.dto.input.SelectedItemDto;
import groupId.artifactId.controller.validator.api.ITokenValidator;
import groupId.artifactId.service.MenuService;

public class TokenValidator implements ITokenValidator {
    private static TokenValidator firstInstance = null;

    public static TokenValidator getInstance() {
        synchronized (TokenValidator.class) {
            if (firstInstance == null) {
                firstInstance = new TokenValidator();
            }
            return firstInstance;
        }
    }

    @Override
    public void validateToken(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalStateException("Error code 500. None of OrderDto have been sent as an input");
        }
        if (orderDto.getSelectedItems() == null) {
            throw new IllegalStateException("Error code 500. None of SelectedItems in OrderDto have been sent as an input");
        }
        for (SelectedItemDto dto : orderDto.getSelectedItems()) {
            if (dto.getMenuItem() == null) {
                throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
            }
            if (dto.getMenuItem().getInfo() == null) {
                throw new IllegalStateException("Error code 500. None of PizzaInfoDto have been sent as an input");
            }
            if (dto.getMenuItem().getInfo().getName() == null || dto.getMenuItem().getInfo().getName().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
            }
            MenuService menuService = MenuService.getInstance();
            if (!menuService.isDishExist(dto.getMenuItem().getInfo().getName())){
                throw new IllegalArgumentException("Error code 400. There is no such dish at the menu");
            }
            if (dto.getMenuItem().getInfo().getDescription() == null || dto.getMenuItem().getInfo().getDescription().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
            }
            if (dto.getMenuItem().getInfo().getSize() <= 0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
            }
            if (dto.getMenuItem().getPrice() <= 0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
            }
            if (dto.getCount() <= 0) {
                throw new IllegalArgumentException("Error code 400. Items count in Order is not valid");
            }
        }
    }
}
