package groupId.artifactId.core.dto.input;

public class MenuItemDtoInput {
    private Double price;
    private Long pizzaInfoId;
    private Long menuId;

    public MenuItemDtoInput() {
    }

    public MenuItemDtoInput(Double price, Long pizzaInfoId, Long menuId) {
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.menuId = menuId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getPizzaInfoId() {
        return pizzaInfoId;
    }

    public void setPizzaInfoId(Long pizzaInfoId) {
        this.pizzaInfoId = pizzaInfoId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "MenuItemDtoInput{" +
                "price=" + price +
                ", pizzaInfoId=" + pizzaInfoId +
                ", menuId=" + menuId +
                '}';
    }
}
