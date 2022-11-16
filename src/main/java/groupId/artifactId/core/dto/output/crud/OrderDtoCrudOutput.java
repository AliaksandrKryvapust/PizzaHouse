package groupId.artifactId.core.dto.output.crud;

import lombok.*;

import java.time.Instant;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OrderDtoCrudOutput {
    private final @NonNull Long id;
    private Instant createdAt;
    private Integer version;
}
