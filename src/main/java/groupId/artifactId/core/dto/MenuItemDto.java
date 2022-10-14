package groupId.artifactId.core.dto;

public class MenuItemDto {
    private PizzaInfoDto pizzaInfo;
    private double price;

    public MenuItemDto() {
    }

    public MenuItemDto(PizzaInfoDto pizzaInfoDto, double price) {
        this.pizzaInfo = pizzaInfoDto;
        this.price = price;
    }

    public void setPizzaInfo(PizzaInfoDto pizzaInfoDto) {
        this.pizzaInfo = pizzaInfoDto;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public PizzaInfoDto getInfo() {
        return this.pizzaInfo;
    }


    public double getPrice() {
        return price;
    }
}
