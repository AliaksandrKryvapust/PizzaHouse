package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizzaInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.Instant;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pizza_info", schema = "pizza_manager")
public class PizzaInfo implements IPizzaInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private Integer size;
    @Generated(GenerationTime.INSERT)
    private Instant creationDate;
    @Generated(GenerationTime.INSERT)
    private Integer version;

    @Override
    public String toString() {
        return "PizzaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
