package groupId.artifactId.storage.entity.api;

import java.time.LocalDateTime;

public interface IToken {
    Integer getId(); // может быть string как возврщаемый тип

    LocalDateTime getCreatAt();

    IOrder getOrder();
}
