package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.LocalDateTime;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private Double price;
    private LocalDateTime creationDate;
    private Integer version;

    public MenuItem() {
    }

    public MenuItem(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public MenuItem(IPizzaInfo pizzaInfo, Double price) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
    }

    public MenuItem(Long id, IPizzaInfo pizzaInfo, Double price, Integer version) {
        this.id = id;
        this.pizzaInfo = pizzaInfo;
        this.price = price;
        this.version = version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPizzaInfo(PizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public PizzaInfo getInfo() {
        return (PizzaInfo) this.pizzaInfo;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", pizzaInfo=" + pizzaInfo +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
