package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.LocalDateTime;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private Double price;
    private Long pizzaInfoId;
    private LocalDateTime creationDate;
    private Integer version;
    private Long menuId;

    public MenuItem() {
    }

    public MenuItem(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public MenuItem(IPizzaInfo pizzaInfo, Double price) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
    }

    public MenuItem(Double price, Long pizzaInfoId, Long menuId) {
        this.price = price;
        this.pizzaInfoId = pizzaInfoId;
        this.menuId = menuId;
    }

    public MenuItem(Long id, IPizzaInfo pizzaInfo, Double price, Integer version) {
        this.id = id;
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.version = version;
    }

    public MenuItem(Long id, IPizzaInfo pizzaInfo, Double price, Long pizzaInfoId, LocalDateTime creationDate,
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
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
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
