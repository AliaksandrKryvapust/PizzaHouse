package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.storage.entity.api.IPizzaInfo;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private double price;

    public MenuItem(IPizzaInfo pizzaInfo, double price) {
        this.pizzaInfo = pizzaInfo;
        this.price=price;
    }

    public void setPizzaInfo(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    @Override
    public IPizzaInfo getInfo() {
        return this.pizzaInfo;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
