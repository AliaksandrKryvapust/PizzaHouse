package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuDao;
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
class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuMapper menuMapper;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction transaction;

    @Test
    void get() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<IMenu> menus = singletonList(Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).build());
        final MenuDtoCrudOutput crudOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(menuDao.get()).thenReturn(menus);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(crudOutput);

        //test
        List<MenuDtoCrudOutput> test = menuService.get();

        // assert
        Assertions.assertEquals(menus.size(), test.size());
        for (MenuDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(enable, output.getEnable());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).build();
        final MenuDtoCrudOutput crudOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(menuDao.get(id)).thenReturn(menu);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(crudOutput);

        //test
        MenuDtoCrudOutput test = menuService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void getRow() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).build();
        Mockito.when(menuDao.getLock(any(Long.class), any(EntityManager.class))).thenReturn(menu);

        //test
        IMenu test = menuService.getRow(id, entityManager);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreationDate());
    }

    @Test
    void getAllData() {
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
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final List<IMenuItem> items = singletonList(MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build());
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).items(items).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        final MenuDtoOutput dtoOutput = MenuDtoOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).items(singletonList(menuItemDtoOutput)).build();
        Mockito.when(menuDao.get(id)).thenReturn(menu);
        Mockito.when(menuMapper.outputMapping(any(IMenu.class))).thenReturn(dtoOutput);

        //test
        MenuDtoOutput test = menuService.getAllData(id);

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
            Assertions.assertEquals(id, output.getPizzaInfo().getId());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
            Assertions.assertEquals(version, output.getPizzaInfo().getVersion());
            Assertions.assertEquals(creationDate, output.getPizzaInfo().getCreatedAt());
        }
    }

    @Test
    void update() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final String inputId = "1";
        final String inputVersion = "1";
        final int version = 1;
        final Instant creationDate = Instant.now();
        final MenuDtoInput menuDtoInput = MenuDtoInput.builder().name(name).enable(enable).build();
        final Menu menu = Menu.builder().name(name).enable(enable).build();
        final Menu menuOutput = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).build();
        final MenuDtoCrudOutput dtoOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuMapper.inputMapping(any(MenuDtoInput.class))).thenReturn(menu);
        Mockito.when(menuDao.update(any(IMenu.class), any(Long.class), any(Integer.class), any(EntityManager.class)))
                .thenReturn(menuOutput);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(dtoOutput);

        //test
        MenuDtoCrudOutput test = menuService.update(menuDtoInput, inputId, inputVersion);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }


    @Test
    void updateItem() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final String pizzaName = "ITALIANO PIZZA";
        final long id = 1L;
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final double price = 18.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(pizzaName).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final List<IMenuItem> items = singletonList(MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build());
        final IMenu menu = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).items(items).build();
        final IMenuItem item = MenuItem.builder().pizzaInfo(pizzaInfo).price(price).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);

        Mockito.when(menuDao.updateItems(any(IMenu.class), any(IMenuItem.class), any(EntityManager.class)))
                .thenReturn(menu);

        //test
        IMenu test = menuService.updateItem(menu, item, entityManager);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        for (IMenuItem output : test.getItems()) {
            Assertions.assertNotNull(output);
            Assertions.assertNotNull(output.getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(id, output.getPizzaInfo().getId());
            Assertions.assertEquals(pizzaName, output.getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getPizzaInfo().getSize());
            Assertions.assertEquals(version, output.getPizzaInfo().getVersion());
            Assertions.assertEquals(creationDate, output.getPizzaInfo().getCreationDate());
        }
    }

    @Test
    void delete() {
        final String inputId = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);

        //test
        menuService.delete(inputId, delete);
        Mockito.verify(menuDao, times(1)).delete(valueId.capture(), valueDelete.capture(), any(EntityManager.class));

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
    }

    @Test
    void save() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final MenuDtoInput menuDtoInput = MenuDtoInput.builder().name(name).enable(enable).build();
        final Menu menu = Menu.builder().name(name).enable(enable).build();
        final Menu menuOutput = Menu.builder().id(id).creationDate(creationDate).version(version).name(name)
                .enable(enable).build();
        final MenuDtoCrudOutput crudOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuMapper.inputMapping(any(MenuDtoInput.class))).thenReturn(menu);
        Mockito.when(menuDao.save(any(IMenu.class), any(EntityManager.class))).thenReturn(menuOutput);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(crudOutput);

        //test
        MenuDtoCrudOutput test = menuService.save(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }
}