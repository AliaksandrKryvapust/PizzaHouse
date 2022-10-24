package groupId.artifactId.core.dto;

public class MenuItemDto {
    private PizzaInfoDto pizzaInfo;
    private double price;

    private Long id;
    private Integer version;

    public MenuItemDto() {
    }

    public MenuItemDto(PizzaInfoDto pizzaInfo, double price, Long id, Integer version) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.id = id;
        this.version = version;
    }

    public void setPizzaInfo(PizzaInfoDto pizzaInfoDto) {
        this.pizzaInfo = pizzaInfoDto;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public PizzaInfoDto getInfo() {
        return this.pizzaInfo;
    }


    public double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }
}
