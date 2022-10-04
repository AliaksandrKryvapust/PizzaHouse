package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IPizzaInfo;
import groupId.artifactId.storage.entity.api.IMenuItem;

public class MenuItem implements IMenuItem {
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

    @Override
    public IPizzaInfo getInfo() {
        return this.pizzaInfo;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
