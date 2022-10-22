package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.LocalDateTime;

public class MenuItem implements IMenuItem {
    private Long id;
    private IPizzaInfo pizzaInfo;
    private Double price;
    private LocalDateTime creationDate;
    private LocalDateTime editDate;

    public MenuItem() {
    }

    public MenuItem(IPizzaInfo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public MenuItem(IPizzaInfo pizzaInfo, Double price) {
        this.pizzaInfo = pizzaInfo;
        this.price = price;
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

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", pizzaInfo=" + pizzaInfo +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", editDate=" + editDate +
                '}';
    }
}
