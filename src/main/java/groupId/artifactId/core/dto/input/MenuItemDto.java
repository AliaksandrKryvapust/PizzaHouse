package groupId.artifactId.core.dto.input;

public class MenuItemDto {
    private PizzaInfoDtoInput pizzaInfo;
    private double price;

    private Long menuId;
    private Integer version;

    public MenuItemDto() {
    }

    public MenuItemDto(PizzaInfoDtoInput pizzaInfo, double price) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
    }

    public MenuItemDto(PizzaInfoDtoInput pizzaInfo, double price, Long menuId) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.menuId = menuId;
    }

    public void setPizzaInfo(PizzaInfoDtoInput pizzaInfoDtoInput) {
        this.pizzaInfo = pizzaInfoDtoInput;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Long menuId) {
        this.menuId = menuId;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public PizzaInfoDtoInput getInfo() {
        return this.pizzaInfo;
    }


    public double getPrice() {
        return price;
    }

    public Long getId() {
        return menuId;
    }

    public Integer getVersion() {
        return version;
    }
}
