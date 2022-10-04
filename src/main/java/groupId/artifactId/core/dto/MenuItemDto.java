package groupId.artifactId.core.dto;

import groupId.artifactId.core.dto.api.IPizzaInfo;

public class MenuItemDto {
    private IPizzaInfo pizzaInfo;
    private double price;

    public MenuItemDto(IPizzaInfo pizzaInfo, double price) {
        this.pizzaInfo = pizzaInfo;
        this.price=price;
    }

    public void setPizzaInfo(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public IPizzaInfo getInfo() {
        return this.pizzaInfo;
    }


    public double getPrice() {
        return price;
    }
}
