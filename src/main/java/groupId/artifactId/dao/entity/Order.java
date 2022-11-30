package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_ticket", schema = "pizza_manager")
public class Order implements IOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(targetEntity = SelectedItem.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @Setter
    private List<ISelectedItem> selectedItems;

    @Override
    public String toString() {
        return "Order{" +
                "selectedItems=" + selectedItems +
                ", id=" + id +
                '}';
    }
}
