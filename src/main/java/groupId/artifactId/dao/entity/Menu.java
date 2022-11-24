package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu", schema = "pizza_manager")
public class Menu implements IMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(targetEntity = MenuItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @Setter
    private List<IMenuItem> items;
    @Setter
    private String name;
    @Setter
    private Boolean enable;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant creationDate;
    @Version
    private Integer version;

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", items=" + items +
                ", name='" + name + '\'' +
                ", enable=" + enable +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
