package groupId.artifactId.core.dto;

import groupId.artifactId.entity.api.IMenuItem;
import groupId.artifactId.entity.api.IPizzaInfo;

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
