package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu_item", schema = "pizza_manager")
public class MenuItem implements IMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @Setter
    private PizzaInfo pizzaInfo;
    @Setter
    private Double price;
    @Generated(GenerationTime.INSERT)
    private Instant creationDate;
    @Version
    private Integer version;

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
