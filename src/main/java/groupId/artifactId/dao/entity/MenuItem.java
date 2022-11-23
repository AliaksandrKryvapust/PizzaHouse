package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu_item", schema = "pizza_manager")
public class MenuItem implements IMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = PizzaInfo.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "pizza_info_id", referencedColumnName = "id")
    @Setter
    private IPizzaInfo pizzaInfo;
    @Setter
    private Double price;
    @Setter
    private Long menuId;
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
                ", menuId=" + menuId +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
