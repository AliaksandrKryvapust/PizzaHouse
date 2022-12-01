package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.dao.entity.api.ITicket;
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
@Table(name = "completed_order", schema = "pizza_manager")
public class CompletedOrder implements ICompletedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = Ticket.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @Setter
    private ITicket ticket;
    @OneToMany(targetEntity = Pizza.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "completed_order_id", referencedColumnName = "id", nullable = false)
    @Setter
    private List<IPizza> items;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant creationDate;

    @Override
    public String toString() {
        return "CompletedOrder{" +
                "ticket=" + ticket +
                ", items=" + items +
                ", id=" + id +
                ", creationDate=" + creationDate +
                '}';
    }
}
