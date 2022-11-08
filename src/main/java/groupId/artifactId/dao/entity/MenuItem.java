package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.Instant;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private Double price;
    private Long pizzaInfoId;
    private Instant creationDate;
    private Integer version;
    private Long menuId;

    public MenuItem() {
    }

    public MenuItem(Double price, Long pizzaInfoId, Long menuId) {
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.menuId = menuId;
    }

    public MenuItem(Long id, Double price, Long pizzaInfoId, Long menuId) {
        this.id = id;
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.menuId = menuId;
    }

    public MenuItem(Long id, IPizzaInfo pizzaInfo, Double price, Long pizzaInfoId, Instant creationDate,
                    Integer version, Long menuId) {
        this.id = id;
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.creationDate = creationDate;
        this.version = version;
        this.menuId = menuId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public IPizzaInfo getInfo() {
        return pizzaInfo;
    }

    public void setPizzaInfo(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Long getPizzaInfoId() {
        return pizzaInfoId;
    }

    public void setPizzaInfoId(Long pizzaInfoId) {
        this.pizzaInfoId = pizzaInfoId;
    }

    @Override
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", pizzaInfo=" + pizzaInfo +
                ", price=" + price +
                ", pizzaInfoId=" + pizzaInfoId +
                ", creationDate=" + creationDate +
                ", version=" + version +
                ", menuId=" + menuId +
                '}';
    }
}
