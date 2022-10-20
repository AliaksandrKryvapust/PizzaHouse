package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private Double price;

    public MenuItem() {
    }

    public MenuItem(IPizzaInfo pizzaInfo, Double price) {
        this.pizzaInfo = pizzaInfo;
        this.price=price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPizzaInfo(PizzaInfo pizzaInfo) {
        this.pizzaInfo = (IPizzaInfo) pizzaInfo;
    }

    public void setPrice(double price) {
        this.price = price;
    }

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
}
