package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
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
class TicketMapperTest {
    @InjectMocks
    private TicketMapper ticketMapper;
    @Mock
    private OrderMapper orderMapper;

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final Instant creationDate = Instant.now();
        final Order order = new Order(id, singletonList(new SelectedItem()));
        final ITicket ticket = new Ticket(id, order, creationDate);

        //test
        TicketDtoCrudOutput test = ticketMapper.outputCrudMapping(ticket);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
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
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final OrderDtoOutput dtoOutput = new OrderDtoOutput(outputs, id);
        Mockito.when(orderMapper.outputMapping(any(IOrder.class))).thenReturn(dtoOutput);

        //test
        TicketDtoOutput test = ticketMapper.outputMapping(ticket);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getOrder().getId());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
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