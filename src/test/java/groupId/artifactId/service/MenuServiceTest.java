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

    @Test
    void get() {
        // preconditions
        final String name = "Optional Menu";
        final boolean enable = false;
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<IMenu> menus = singletonList(new Menu(id, creationDate, version, name, enable));
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
        final IMenu menu = new Menu(id, creationDate, version, name, enable);
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
        List<IMenuItem> items = singletonList(new MenuItem(id, new PizzaInfo(id, pizzaName,
                description, size, creationDate, version), price, id, creationDate, version, id));
        final IMenu menu = new Menu(items, id, creationDate, version, name, enable);
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(pizzaName).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price).pizzaInfoId(id)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        final MenuDtoOutput dtoOutput = MenuDtoOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).items(singletonList(menuItemDtoOutput)).build();
        Mockito.when(menuDao.getAllData(id)).thenReturn(menu);
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
            Assertions.assertEquals(id, output.getPizzaInfoId());
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
    void isIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(menuDao.exist(id)).thenReturn(true);

        //test
        Boolean test = menuService.isIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void exist() {
        // preconditions
        final String name = "name";
        Mockito.when(menuDao.doesMenuExist(name)).thenReturn(true);

        //test
        Boolean test = menuService.exist(name);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
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
        final Menu menu = new Menu(name, enable);
        final Menu menuOutput = new Menu(id, name, enable);
        final MenuDtoCrudOutput dtoOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(menuMapper.inputMapping(any(MenuDtoInput.class))).thenReturn(menu);
        Mockito.when(menuDao.update(any(IMenu.class), any(Long.class), any(Integer.class))).thenReturn(menuOutput);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(dtoOutput);

        //test
        MenuDtoCrudOutput test = menuService.update(menuDtoInput, inputId, inputVersion);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
    }

    @Test
    void delete() {
        final String inputId = "1";
        final String version = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> valueVersion = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);

        //test
        menuService.delete(inputId, version, delete);
        Mockito.verify(menuDao, times(1)).delete(valueId.capture(), valueVersion.capture(), valueDelete.capture());

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersion.getValue());
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
        final Menu menu = new Menu(name, enable);
        final Menu menuOutput = new Menu(id, name, enable);
        final MenuDtoCrudOutput crudOutput = MenuDtoCrudOutput.builder().id(id).createdAt(creationDate).version(version)
                .name(name).enable(enable).build();
        Mockito.when(menuMapper.inputMapping(any(MenuDtoInput.class))).thenReturn(menu);
        Mockito.when(menuDao.save(any(IMenu.class))).thenReturn(menuOutput);
        Mockito.when(menuMapper.outputCrudMapping(any(IMenu.class))).thenReturn(crudOutput);

        //test
        MenuDtoCrudOutput test = menuService.save(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(enable, test.getEnable());
    }
}