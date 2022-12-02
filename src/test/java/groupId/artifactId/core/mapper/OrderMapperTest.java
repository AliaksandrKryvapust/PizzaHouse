package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
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
class OrderMapperTest {
    @InjectMocks
    private OrderMapper orderMapper;
    @Mock
    private SelectedItemMapper selectedItemMapper;

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
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        SelectedItemDtoOutput outputs = SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build();
        final IOrder order = new Order(id, selectedItems);
        Mockito.when(selectedItemMapper.outputMapping(any(ISelectedItem.class))).thenReturn(outputs);

        //test
        OrderDtoOutput test = orderMapper.outputMapping(order);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        for (SelectedItemDtoOutput output : test.getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreatedAt());
            Assertions.assertEquals(version, output.getMenuItem().getVersion());
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
        }
    }
}