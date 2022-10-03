package groupId.artifactId.entity.api;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public interface IToken {
    AtomicInteger getId(); // может быть string как возврщаемый тип

    LocalDateTime getCreatAt();

    IOrder getOrder();
}
