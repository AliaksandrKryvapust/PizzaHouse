package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PizzaInfoServiceTest {
    @InjectMocks
    private PizzaInfoService pizzaInfoService;
    @Mock
    private PizzaInfoDao pizzaInfoDao;
    @Mock
    private PizzaInfoMapper pizzaInfoMapper;
    @Mock
    EntityManager entityManager;
    @Mock
    EntityTransaction transaction;

    @Test
    void save() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(name).description(description)
                .size(size).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final PizzaInfo pizzaInfoOutput = PizzaInfo.builder().id(id).name(name).description(description).size(size).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(pizzaInfoMapper.inputMapping(any(PizzaInfoDtoInput.class))).thenReturn(pizzaInfo);
        Mockito.when(pizzaInfoDao.save(any(IPizzaInfo.class), any(EntityManager.class))).thenReturn(pizzaInfoOutput);
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(pizzaInfoDtoOutput);

        //test
        PizzaInfoDtoOutput test = pizzaInfoService.save(pizzaInfoDtoInput);

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
    void get() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<IPizzaInfo> pizzaInfos = singletonList(PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        Mockito.when(pizzaInfoDao.get()).thenReturn(pizzaInfos);
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(pizzaInfoDtoOutput);

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
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IPizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        Mockito.when(pizzaInfoDao.get(id)).thenReturn(pizzaInfo);
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(pizzaInfoDtoOutput);

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
    void update() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long id = 1L;
        final String inputId = "1";
        final String inputVersion = "1";
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(name).description(description)
                .size(size).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final PizzaInfo pizzaInfoOutput = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(pizzaInfoMapper.inputMapping(any(PizzaInfoDtoInput.class))).thenReturn(pizzaInfo);
        Mockito.when(pizzaInfoDao.update(any(IPizzaInfo.class), any(Long.class), any(Integer.class),
                any(EntityManager.class))).thenReturn(pizzaInfoOutput);
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(pizzaInfoDtoOutput);

        //test
        PizzaInfoDtoOutput test = pizzaInfoService.update(pizzaInfoDtoInput, inputId, inputVersion);

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
    void delete() {
        final String inputId = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);

        //test
        pizzaInfoService.delete(inputId, delete);
        Mockito.verify(pizzaInfoDao, times(1)).delete(valueId.capture(), valueDelete.capture());

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
    }
}