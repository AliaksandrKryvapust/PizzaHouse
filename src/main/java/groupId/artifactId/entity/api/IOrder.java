package groupId.artifactId.entity.api;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface IOrder {
    List<ISelectedItem> getSelected();
}
