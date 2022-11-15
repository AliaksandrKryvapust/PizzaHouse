package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.IPizza;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class PizzaMapperTest {
    @InjectMocks
    private PizzaMapper pizzaMapper;

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final String name = "ITALIANO PIZZA";
        final int size = 32;
        final Instant creationDate = Instant.now();
        IPizza pizza = new Pizza(id, id, name, size, creationDate, version);

        //test
        PizzaDtoOutput test = pizzaMapper.outputMapping(pizza);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getCompletedOrderId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(size, test.getSize());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
    }
}