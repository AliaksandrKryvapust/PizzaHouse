package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {
    @InjectMocks
    private MenuItemService menuItemService;
    @Mock
    private MenuItemDao menuItemDao;
    @Mock
    private MenuItemMapper menuItemMapper;
    @Mock
    EntityManager entityManager;
    @Mock
    EntityTransaction transaction;

    @Test
    void save() {
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
        final IMenuItem menuItemInput = MenuItem.builder().price(price).menuId(id).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).pizzaInfoId(id).menuId(id)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        final MenuItem menuItemOutput = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).menuId(id).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItemInput);
        Mockito.when(menuItemDao.save(any(IMenuItem.class), any(EntityManager.class))).thenReturn(menuItemOutput);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.save(menuDtoInput);

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

    @Test
    void get() {
        // preconditions
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<IMenuItem> menuItems = singletonList(MenuItem.builder().id(id).price(price)
                .creationDate(creationDate).version(version).menuId(id).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(menuItemDao.get()).thenReturn(menuItems);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        List<MenuItemDtoOutput> test = menuItemService.get();

        // assert
        Assertions.assertEquals(menuItems.size(), test.size());
        for (MenuItemDtoOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertNotNull(output.getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getMenuId());
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(id, output.getPizzaInfo().getId());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
            Assertions.assertEquals(version, output.getPizzaInfo().getVersion());
            Assertions.assertEquals(creationDate, output.getPizzaInfo().getCreatedAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).build();
        final IMenuItem menuItem = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).menuId(id).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(menuItemDao.get(id)).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.get(id);

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

    @Test
    void update() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final String inputId = "1";
        final String inputVersion = "1";
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(pizzaName).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).pizzaInfoId(id).menuId(id)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        final MenuItem menuItem = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).menuId(id).build();
        final MenuItem menuItemInput = MenuItem.builder().price(price).pizzaInfo(pizzaInfo).menuId(id).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItemInput);
        Mockito.when(menuItemDao.update(any(IMenuItem.class), any(Long.class), any(Integer.class),
                any(EntityManager.class))).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.update(menuDtoInput, inputId, inputVersion);

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

    @Test
    void delete() {
        final String inputId = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);

        //test
        menuItemService.delete(inputId, delete);
        Mockito.verify(menuItemDao, times(1)).delete(valueId.capture(), valueDelete.capture(), any(EntityManager.class));

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
    }
}