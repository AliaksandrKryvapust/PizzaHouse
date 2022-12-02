package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuMapperTest {
    @InjectMocks
    private MenuMapper menuMapper;
    @Mock
    private MenuItemMapper menuItemMapper;

    @Test
    void inputMappingConditionOne() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final MenuDtoInput menuDtoInput = MenuDtoInput.builder().name(name).enable(enable).build();

        //test
        IMenu test = menuMapper.inputMapping(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
    }

    @Test
    void inputMappingConditionTwo() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final double price = 18.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description).size(size)
                .build();
        final List<MenuItemDtoInput> menuItemsInput = Collections.singletonList(MenuItemDtoInput.builder()
                .pizzaInfoDtoInput(pizzaInfoDtoInput).price(price).build());
        final MenuDtoInput menuDtoInput = MenuDtoInput.builder().name(name).enable(enable).items(menuItemsInput).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(pizzaName).description(description).size(size).build();
        final IMenuItem menuItems = MenuItem.builder().pizzaInfo(pizzaInfo).price(price).build();
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItems);

        //test
        IMenu test = menuMapper.inputMapping(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getItems());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        for (IMenuItem input : test.getItems()) {
            Assertions.assertNotNull(input);
            Assertions.assertNotNull(input.getPizzaInfo());
            Assertions.assertEquals(price, input.getPrice());
            Assertions.assertEquals(pizzaName, input.getPizzaInfo().getName());
            Assertions.assertEquals(description, input.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, input.getPizzaInfo().getSize());
        }
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name).enable(enable).build();

        //test
        MenuDtoCrudOutput test = menuMapper.outputCrudMapping(menu);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void outputMapping() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final double price = 18.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final List<IMenuItem> menuItems = Collections.singletonList(MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build());
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name).enable(enable)
                .items(menuItems).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(menuItemDtoOutput);

        //test
        MenuDtoOutput test = menuMapper.outputMapping(menu);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        for (MenuItemDtoOutput output : test.getItems()) {
            Assertions.assertNotNull(output);
            Assertions.assertNotNull(output.getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
        }
    }
}