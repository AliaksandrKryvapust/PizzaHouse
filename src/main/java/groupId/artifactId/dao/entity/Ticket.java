package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ITicket;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket", schema = "pizza_manager")
public class Ticket implements ITicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = Order.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @Setter
    private IOrder order;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant createAt;

    @Override
    public String toString() {
        return "Ticket{" +
                "order=" + order +
                ", id=" + id +
                ", createAt=" + createAt +
                '}';
    }
}
