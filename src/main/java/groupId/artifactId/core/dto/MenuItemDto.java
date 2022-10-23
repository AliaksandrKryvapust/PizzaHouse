package groupId.artifactId.core.dto;

public class MenuItemDto {
    private PizzaInfoDto pizzaInfo;
    private double price;

    private Integer id;

    public MenuItemDto() {
    }

    public MenuItemDto(PizzaInfoDto pizzaInfo, double price, Integer id) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.id = id;
    }

    public void setPizzaInfo(PizzaInfoDto pizzaInfoDto) {
        this.pizzaInfo = pizzaInfoDto;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PizzaInfoDto getInfo() {
        return this.pizzaInfo;
    }


    public double getPrice() {
        return price;
    }

    public Long getId() {
        return Long.valueOf(id);
    }
}
