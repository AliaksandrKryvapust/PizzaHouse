package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuItemMapperTest {
    @InjectMocks
    private MenuItemMapper menuItemMapper;
    @Mock
    private PizzaInfoMapper pizzaInfoMapper;

    @Test
    void inputMapping() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(price, id, id);

        //test
        IMenuItem test = menuItemMapper.inputMapping(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(price, test.getPrice());
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenuItem menuItem = new MenuItem(id, null, price, id, creationDate, version, id);

        //test
        MenuItemDtoCrudOutput test = menuItemMapper.outputCrudMapping(menuItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreation_date());
    }

    @Test
    void outputMappingConditionOne() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenuItem menuItem = new MenuItem(id, new PizzaInfo(id, pizzaName, description, size, creationDate, version),
                price, id, creationDate, version, id);
        final PizzaInfoDtoOutput dtoOutput = new PizzaInfoDtoOutput(id, pizzaName, description, size, creationDate, version);
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemMapper.outputMapping(menuItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreation_date());
        Assertions.assertEquals(id, test.getPizzaInfo().getId());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
        Assertions.assertEquals(version, test.getPizzaInfo().getVersion());
        Assertions.assertEquals(creationDate, test.getPizzaInfo().getCreatedAt());
    }

    @Test
    void outputMappingConditionTwo() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenuItem menuItem = new MenuItem(id, null, price, id, creationDate, version, id);

        //test
        MenuItemDtoOutput test = menuItemMapper.outputMapping(menuItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreation_date());
    }
}