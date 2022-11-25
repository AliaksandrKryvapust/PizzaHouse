package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "selected_item", schema = "pizza_manager")
public class SelectedItem implements ISelectedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = MenuItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", referencedColumnName = "id")
    @Setter
    private IMenuItem menuItem;
    @Setter
    private Long orderId;
    @Setter
    private Integer count;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant createAt;
    @Version
    private Integer version;

    @Override
    public String toString() {
        return "SelectedItem{" +
                "menuItem=" + menuItem +
                ", id=" + id +
                ", count=" + count +
                ", createAt=" + createAt +
                ", version=" + version +
                '}';
    }
}
