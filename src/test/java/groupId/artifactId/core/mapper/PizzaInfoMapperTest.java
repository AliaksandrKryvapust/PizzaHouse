package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class PizzaInfoMapperTest {
    @InjectMocks
    private PizzaInfoMapper pizzaInfoMapper;

    @Test
    void inputMapping() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(name).description(description)
                .size(size).build();

        //test
        IPizzaInfo test = pizzaInfoMapper.inputMapping(pizzaInfoDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(description, test.getDescription());
        Assertions.assertEquals(size, test.getSize());
    }

    @Test
    void outputMapping() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IPizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();

        //test
        PizzaInfoDtoOutput test = pizzaInfoMapper.outputMapping(pizzaInfo);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(description, test.getDescription());
        Assertions.assertEquals(size, test.getSize());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }
}