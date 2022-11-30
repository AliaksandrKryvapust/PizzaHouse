package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrderStage;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_stage", schema = "pizza_manager")
public class OrderStage implements IOrderStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String description;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant creationDate;

    @Override
    public String toString() {
        return "OrderStage{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
