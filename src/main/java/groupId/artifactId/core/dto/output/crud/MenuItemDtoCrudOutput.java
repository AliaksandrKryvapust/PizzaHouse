package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class MenuItemDtoCrudOutput {
    private Long id;
    private Double price;
    private Long pizzaInfoId;
    private Instant creation_date;
    private Integer version;
    private Long menuId;

    public MenuItemDtoCrudOutput() {
    }

    public MenuItemDtoCrudOutput(Long id, Double price, Long pizzaInfoId, Long menuId) {
        this.id = id;
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.menuId = menuId;
    }

    public MenuItemDtoCrudOutput(Long id, Double price, Long pizzaInfoId, Instant creation_date, Integer version, Long menuId) {
        this.id = id;
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.creation_date = creation_date;
        this.version = version;
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Instant creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
