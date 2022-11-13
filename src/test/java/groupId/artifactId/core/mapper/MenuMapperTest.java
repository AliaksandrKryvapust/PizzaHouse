package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
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
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuMapperTest {
    @InjectMocks
    private MenuMapper menuMapper;
    @Mock
    private MenuItemMapper menuItemMapper;

    @Test
    void inputMapping() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final MenuDtoInput menuDtoInput = new MenuDtoInput(name, enable);

        //test
        IMenu test = menuMapper.inputMapping(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenu menu = new Menu(id, creationDate, version, name, enable);

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
        List<IMenuItem> items = singletonList(new MenuItem(id, new PizzaInfo(id, pizzaName,
                description, size, creationDate, version), price, id, creationDate, version, id));
        final IMenu menu = new Menu(items, id, creationDate, version, name, enable);
        final MenuItemDtoOutput dtoOutput = new MenuItemDtoOutput(id, price, id, creationDate, version, id,
                new PizzaInfoDtoOutput(id, pizzaName, description, size, creationDate, version));
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

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
            Assertions.assertEquals(id, output.getPizzaInfoId());
            Assertions.assertEquals(id, output.getMenuId());
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreation_date());
            Assertions.assertEquals(id, output.getPizzaInfo().getId());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
            Assertions.assertEquals(version, output.getPizzaInfo().getVersion());
            Assertions.assertEquals(creationDate, output.getPizzaInfo().getCreatedAt());
        }
    }
}