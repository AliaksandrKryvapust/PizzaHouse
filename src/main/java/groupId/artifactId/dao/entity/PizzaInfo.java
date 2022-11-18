package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizzaInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Instant creationDate;
    private Integer version;
}
