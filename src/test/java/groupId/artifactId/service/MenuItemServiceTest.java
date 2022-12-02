package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenu;
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
    private MenuService menuService;
    @Mock
    EntityManager entityManager;
    @Mock
    EntityTransaction transaction;

    @Test
    void saveConditionOne() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(pizzaName).description(description).size(size).build();
        final IMenuItem menuItemInput = MenuItem.builder().price(price).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        final MenuItem menuItemOutput = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
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
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
    }

    @Test
    void saveConditionTwo() {
        // preconditions
        final long id = 1L;
        final String name = "Optional Menu";
        final boolean enable = false;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final IMenuItem menuItemInput = MenuItem.builder().price(price).build();
        final List<IMenuItem> items = singletonList(MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build());
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).items(items).build();
        final MenuItem menuItemOutput = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).menuId(id)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItemInput);
        Mockito.when(menuItemDao.save(any(IMenuItem.class), any(EntityManager.class))).thenReturn(menuItemOutput);
        Mockito.when(menuService.getRow(any(Long.class), any(EntityManager.class))).thenReturn(menu);
        Mockito.when(menuService.updateItem(any(Menu.class),any(MenuItem.class), any(EntityManager.class))).thenReturn(menu);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.save(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
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
                .creationDate(creationDate).version(version).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
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
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
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
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(pizzaName).description(description).size(size).build();
        final IMenuItem menuItem = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(menuItemDao.get(id)).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
    }

    @Test
    void updateConditionOne() {
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
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(pizzaName).description(description).size(size).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        final MenuItem menuItem = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItemInput = MenuItem.builder().price(price).pizzaInfo(pizzaInfo).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
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
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
    }

    @Test
    void updateConditionTwo() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final String name = "Optional Menu";
        final boolean enable = false;
        final int version = 1;
        final String inputId = "1";
        final String inputVersion = "1";
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).menuId(id).build();
        final List<IMenuItem> items = singletonList(MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build());
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).items(items).build();
        final MenuItem menuItemInput = MenuItem.builder().price(price).pizzaInfo(pizzaInfo).build();
        final MenuItem menuItem = MenuItem.builder().id(id).price(price).pizzaInfo(pizzaInfo)
                .creationDate(creationDate).version(version).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoOutput dtoOutput = MenuItemDtoOutput.builder().id (id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItemInput);
        Mockito.when(menuItemDao.update(any(IMenuItem.class), any(Long.class), any(Integer.class),
                any(EntityManager.class))).thenReturn(menuItem);
        Mockito.when(menuService.getRow(any(Long.class), any(EntityManager.class))).thenReturn(menu);
        Mockito.when(menuService.updateItem(any(Menu.class),any(MenuItem.class), any(EntityManager.class))).thenReturn(menu);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.update(menuDtoInput, inputId, inputVersion);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(pizzaName, test.getPizzaInfo().getName());
        Assertions.assertEquals(description, test.getPizzaInfo().getDescription());
        Assertions.assertEquals(size, test.getPizzaInfo().getSize());
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