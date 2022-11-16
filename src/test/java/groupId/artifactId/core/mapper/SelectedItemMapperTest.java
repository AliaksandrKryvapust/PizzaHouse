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
        final SelectedItemDtoInput dtoInput = new SelectedItemDtoInput(id, count);

        //test
        ISelectedItem test = selectedItemMapper.inputMapping(dtoInput, id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getMenuItemId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(count, test.getCount());
    }

    @Test
    void outputMappingConditionOne() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final ISelectedItem selectedItem = new SelectedItem(new MenuItem(id,
                new PizzaInfo(id, name, description, size, creationDate, version), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version);
        final MenuItemDtoOutput output = new MenuItemDtoOutput(id,
                price, id, creationDate, version, id, new PizzaInfoDtoOutput(id, name, description, size, creationDate, version));
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(output);

        //test
        SelectedItemDtoOutput test = selectedItemMapper.outputMapping(selectedItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getMenuItem());
        Assertions.assertNotNull(test.getMenuItem().getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(id, test.getMenuItemId());
        Assertions.assertEquals(count, test.getCount());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getMenuItem().getId());
        Assertions.assertEquals(price, test.getMenuItem().getPrice());
        Assertions.assertEquals(id, test.getMenuItem().getPizzaInfoId());
        Assertions.assertEquals(id, test.getMenuItem().getMenuId());
        Assertions.assertEquals(creationDate, test.getMenuItem().getCreatedAt());
        Assertions.assertEquals(version, test.getMenuItem().getVersion());
        Assertions.assertEquals(id, test.getMenuItem().getPizzaInfo().getId());
        Assertions.assertEquals(name, test.getMenuItem().getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getMenuItem().getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getMenuItem().getPizzaInfo().getSize());
        Assertions.assertEquals(creationDate, test.getMenuItem().getPizzaInfo().getCreatedAt());
        Assertions.assertEquals(version, test.getMenuItem().getPizzaInfo().getVersion());
    }

    @Test
    void outputMappingConditionTwo() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final Instant creationDate = Instant.now();
        final ISelectedItem selectedItem = new SelectedItem(null, id, id, id, count, creationDate, version);

        //test
        SelectedItemDtoOutput test = selectedItemMapper.outputMapping(selectedItem);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(id, test.getMenuItemId());
        Assertions.assertEquals(count, test.getCount());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
    }
}