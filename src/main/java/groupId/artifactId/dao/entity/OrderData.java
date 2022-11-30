package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
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
@Table(name = "order_data", schema = "pizza_manager")
public class OrderData implements IOrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = Ticket.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @Setter
    private ITicket ticket;
    @OneToMany(targetEntity = OrderStage.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_data_id", referencedColumnName = "id", nullable = false)
    @Setter
    private List<IOrderStage> orderHistory;
    @Setter
    private Boolean done;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant creationDate;

    @Override
    public String toString() {
        return "OrderData{" +
                "ticket=" + ticket +
                ", orderHistory=" + orderHistory +
                ", id=" + id +
                ", done=" + done +
                ", creationDate=" + creationDate +
                '}';
    }
}
