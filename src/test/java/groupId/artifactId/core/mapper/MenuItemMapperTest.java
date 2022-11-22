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
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();

        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).pizzaInfoId(id).menuId(id).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(pizzaName).description(description).size(size)
                .creationDate(creationDate).version(version).build();

        //test
        IMenuItem test = menuItemMapper.inputMapping(menuDtoInput, pizzaInfo);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(id, test.getPizzaInfo().getId());
        Assertions.assertEquals(price, test.getPrice());
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).build();
        final MenuItem menuItem = MenuItem.builder()
                .id(id)
                .price(price)
                .pizzaInfo(pizzaInfo)
                .creationDate(creationDate)
                .version(version)
                .menuId(id).build();

        //test
        MenuItemDtoCrudOutput test = menuItemMapper.outputCrudMapping(menuItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(pizzaName).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).menuId(id).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        Mockito.when(pizzaInfoMapper.outputMapping(any(IPizzaInfo.class))).thenReturn(pizzaInfoDtoOutput);

        //test
        MenuItemDtoOutput test = menuItemMapper.outputMapping(menuItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getPizzaInfo().getId());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
        Assertions.assertEquals(version, test.getPizzaInfo().getVersion());
        Assertions.assertEquals(creationDate, test.getPizzaInfo().getCreatedAt());
    }
}