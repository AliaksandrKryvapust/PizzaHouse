package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDtoCrudOutput;
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
    void inputMapping() {
        // preconditions
        final long id = 1L;
        final int count = 5;
        final OrderDtoInput orderDtoInput = new OrderDtoInput(singletonList(new SelectedItemDtoInput(id, count)));
        final ISelectedItem selectedItem = new SelectedItem(id, id, count);
        Mockito.when(selectedItemMapper.inputMapping(any(SelectedItemDtoInput.class), any(Long.class))).thenReturn(selectedItem);

        //test
        IOrder test = orderMapper.inputMapping(orderDtoInput, id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        for (ISelectedItem item : test.getSelectedItems()) {
            Assertions.assertEquals(id, item.getOrderId());
            Assertions.assertEquals(id, item.getMenuItemId());
            Assertions.assertEquals(count, item.getCount());
        }
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IOrder order = new Order(id, creationDate, version);

        //test
        OrderDtoCrudOutput test = orderMapper.outputCrudMapping(order);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
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
        List<ISelectedItem> selectedItems = singletonList(new SelectedItem(new MenuItem(id,
                new PizzaInfo(id, name, description, size, creationDate, version), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version));
        SelectedItemDtoOutput outputs = new SelectedItemDtoOutput(new MenuItemDtoOutput(id,
                price, id, creationDate, version, id, new PizzaInfoDtoOutput(id, name, description, size, creationDate, version)),
                id, id, id, count, creationDate, version);
        final IOrder order = new Order(selectedItems, id, creationDate, version);
        Mockito.when(selectedItemMapper.outputMapping(any(ISelectedItem.class))).thenReturn(outputs);

        //test
        OrderDtoOutput test = orderMapper.outputMapping(order);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
        for (SelectedItemDtoOutput output : test.getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderId());
            Assertions.assertEquals(id, output.getMenuItemId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreateAt());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfoId());
            Assertions.assertEquals(id, output.getMenuItem().getMenuId());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreation_date());
            Assertions.assertEquals(version, output.getMenuItem().getVersion());
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfo().getId());
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
            Assertions.assertEquals(creationDate, output.getMenuItem().getPizzaInfo().getCreatedAt());
            Assertions.assertEquals(version, output.getMenuItem().getPizzaInfo().getVersion());
        }
    }
}