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
        final int version = 1;
        final Instant creationDate = Instant.now();
        final ITicket ticket = new Ticket(id, id, creationDate, version);

        //test
        TicketDtoCrudOutput test = ticketMapper.outputCrudMapping(ticket);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
        Assertions.assertEquals(version, test.getVersion());
    }

    @Test
    void outputMappingConditionOne() {
        // preconditions
        final long id = 1L;
        final long orderId = 1L;
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
        List<SelectedItemDtoOutput> outputs = singletonList(new SelectedItemDtoOutput(new MenuItemDtoOutput(id,
                price, id, creationDate, version, id, new PizzaInfoDtoOutput(id, name, description, size, creationDate, version)),
                id, id, id, count, creationDate, version));
        final ITicket ticket = new Ticket(new Order(selectedItems, id, creationDate, version), id, orderId, creationDate, version);
        final OrderDtoOutput dtoOutput = new OrderDtoOutput(outputs, id, creationDate, version);
        Mockito.when(orderMapper.outputMapping(any(IOrder.class))).thenReturn(dtoOutput);

        //test
        TicketDtoOutput test = ticketMapper.outputMapping(ticket);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(orderId, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getOrder().getId());
        Assertions.assertEquals(creationDate, test.getOrder().getCreationDate());
        Assertions.assertEquals(version, test.getOrder().getVersion());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
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

    @Test
    void outputMappingConditionTwo() {
        // preconditions
        final long id = 1L;
        final long orderId = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final ITicket ticket = new Ticket(null, id, orderId, creationDate, version);

        //test
        TicketDtoOutput test = ticketMapper.outputMapping(ticket);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(orderId, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
        Assertions.assertEquals(version, test.getVersion());
    }
}