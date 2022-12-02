package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SelectedItemMapperTest {
    @InjectMocks
    private SelectedItemMapper selectedItemMapper;
    @Mock
    private MenuItemMapper menuItemMapper;

    @Test
    void inputMapping() {
        // preconditions
        final long id = 1L;
        final int count = 5;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final SelectedItemDtoInput dtoInput = SelectedItemDtoInput.builder().menuItemId(id).count(count).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();

        //test
        ISelectedItem test = selectedItemMapper.inputMapping(dtoInput, singletonList(menuItem));

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getMenuItem());
        Assertions.assertNotNull(test.getMenuItem().getPizzaInfo());
        Assertions.assertEquals(count, test.getCount());
        Assertions.assertEquals(id, test.getMenuItem().getId());
        Assertions.assertEquals(price, test.getMenuItem().getPrice());
        Assertions.assertEquals(creationDate, test.getMenuItem().getCreationDate());
        Assertions.assertEquals(version, test.getMenuItem().getVersion());
        Assertions.assertEquals(name, test.getMenuItem().getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getMenuItem().getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getMenuItem().getPizzaInfo().getSize());
    }

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        final ISelectedItem selectedItem = SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(menuItemDtoOutput);

        //test
        SelectedItemDtoOutput test = selectedItemMapper.outputMapping(selectedItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getMenuItem());
        Assertions.assertNotNull(test.getMenuItem().getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(count, test.getCount());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getMenuItem().getId());
        Assertions.assertEquals(price, test.getMenuItem().getPrice());
        Assertions.assertEquals(creationDate, test.getMenuItem().getCreatedAt());
        Assertions.assertEquals(version, test.getMenuItem().getVersion());
        Assertions.assertEquals(name, test.getMenuItem().getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getMenuItem().getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getMenuItem().getPizzaInfo().getSize());
    }
}