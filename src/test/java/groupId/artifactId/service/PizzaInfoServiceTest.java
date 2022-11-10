package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PizzaInfoServiceTest {
    @InjectMocks
    private PizzaInfoService pizzaInfoService;
    @Mock
    private PizzaInfoDao pizzaInfoDao;

    @Test
    void save() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final PizzaInfoDtoInput pizzaInfoDtoInput = new PizzaInfoDtoInput(name, description, size);
        Mockito.when(pizzaInfoDao.save(any(IPizzaInfo.class))).thenReturn(new PizzaInfo(id, name, description, size));

        //test
        PizzaInfoDtoOutput test = pizzaInfoService.save(pizzaInfoDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(description, test.getDescription());
        Assertions.assertEquals(size, test.getSize());
    }

    @Test
    void get() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final int version =1;
        final Instant creationDate = Instant.now();
        List<IPizzaInfo> pizzaInfos = singletonList(new PizzaInfo(id, name, description, size, creationDate, version));
        Mockito.when(pizzaInfoDao.get()).thenReturn(pizzaInfos);

        //test
        List<PizzaInfoDtoOutput> test = pizzaInfoService.get();

        // assert
        Assertions.assertEquals(pizzaInfos.size(), test.size());
        for (PizzaInfoDtoOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(description, output.getDescription());
            Assertions.assertEquals(size, output.getSize());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final int version =1;
        final Instant creationDate = Instant.now();
        final IPizzaInfo pizzaInfo = new PizzaInfo(id, name, description, size, creationDate, version);
        Mockito.when(pizzaInfoDao.get(id)).thenReturn(pizzaInfo);

        //test
        PizzaInfoDtoOutput test = pizzaInfoService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(description, test.getDescription());
        Assertions.assertEquals(size, test.getSize());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void isIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(pizzaInfoDao.exist(id)).thenReturn(true);

        //test
        Boolean test = pizzaInfoService.isIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void exist() {
        // preconditions
        final String name = "name";
        Mockito.when(pizzaInfoDao.doesPizzaExist(name)).thenReturn(true);

        //test
        Boolean test = pizzaInfoService.exist(name);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void update() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final String inputId = "1";
        final String version = "1";
        final PizzaInfoDtoInput pizzaInfoDtoInput = new PizzaInfoDtoInput(name, description, size);
        Mockito.when(pizzaInfoDao.update(any(IPizzaInfo.class), eq(Long.valueOf(inputId)), eq(Integer.valueOf(version)))).
                thenReturn(new PizzaInfo(id, name, description, size));

        //test
        PizzaInfoDtoOutput test = pizzaInfoService.update(pizzaInfoDtoInput, inputId, version);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(description, test.getDescription());
        Assertions.assertEquals(size, test.getSize());
    }

    @Test
    void delete() {
        final String inputId = "1";
        final String version = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> valueVersion = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);

        //test
        pizzaInfoService.delete(inputId, version, delete);
        Mockito.verify(pizzaInfoDao, times(1)).delete(valueId.capture(), valueVersion.capture(), valueDelete.capture());

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersion.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
    }
}