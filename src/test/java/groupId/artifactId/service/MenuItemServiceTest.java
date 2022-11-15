package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
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

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(price, id, id);
        final MenuItem menuItem = new MenuItem(price, id, id);
        final MenuItem menuItemOutput = new MenuItem(id, price, id, id);
        final MenuItemDtoCrudOutput dtoCrudOutput = new MenuItemDtoCrudOutput(id, price, id, id);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(menuItem);
        Mockito.when(menuItemDao.save(any(IMenuItem.class))).thenReturn(menuItemOutput);
        Mockito.when(menuItemMapper.outputCrudMapping(any(IMenuItem.class))).thenReturn(dtoCrudOutput);

        //test
        MenuItemDtoCrudOutput test = menuItemService.save(menuDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(price, test.getPrice());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<IMenuItem> menuItems = singletonList(new MenuItem(id, new PizzaInfo(), price, id, creationDate, version, id));
        final MenuItemDtoCrudOutput crudOutput = new MenuItemDtoCrudOutput(id, price, id, creationDate, version, id);
        Mockito.when(menuItemDao.get()).thenReturn(menuItems);
        Mockito.when(menuItemMapper.outputCrudMapping(any(IMenuItem.class))).thenReturn(crudOutput);

        //test
        List<MenuItemDtoCrudOutput> test = menuItemService.get();

        // assert
        Assertions.assertEquals(menuItems.size(), test.size());
        for (MenuItemDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getPizzaInfoId());
            Assertions.assertEquals(id, output.getMenuId());
            Assertions.assertEquals(price, output.getPrice());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(creationDate, output.getCreation_date());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenuItem menuItem = new MenuItem(id, new PizzaInfo(), price, id, creationDate, version, id);
        final MenuItemDtoCrudOutput crudOutput = new MenuItemDtoCrudOutput(id, price, id, creationDate, version, id);
        Mockito.when(menuItemDao.get(id)).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputCrudMapping(any(IMenuItem.class))).thenReturn(crudOutput);

        //test
        MenuItemDtoCrudOutput test = menuItemService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(price, test.getPrice());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(creationDate, test.getCreation_date());
    }

    @Test
    void getAllData() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final IMenuItem menuItem = new MenuItem(id, new PizzaInfo(id, pizzaName, description, size, creationDate, version),
                price, id, creationDate, version, id);
        final MenuItemDtoOutput dtoOutput = new MenuItemDtoOutput(id, price, id, creationDate, version, id,
                new PizzaInfoDtoOutput(id, pizzaName, description, size, creationDate, version));
        Mockito.when(menuItemDao.getAllData(id)).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputMapping(any(IMenuItem.class))).thenReturn(dtoOutput);

        //test
        MenuItemDtoOutput test = menuItemService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getPizzaInfo());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
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
    void isIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(menuItemDao.exist(id)).thenReturn(true);

        //test
        Boolean test = menuItemService.isIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void update() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String inputId = "1";
        final String version = "1";
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(price, id, id);
        final MenuItem menuItem = new MenuItem(id, price, id, id);
        final MenuItemDtoCrudOutput dtoCrudOutput = new MenuItemDtoCrudOutput(id, price, id, id);
        Mockito.when(menuItemMapper.inputMapping(any(MenuItemDtoInput.class))).thenReturn(new MenuItem(price, id, id));
        Mockito.when(menuItemDao.update(any(IMenuItem.class), any(Long.class), any(Integer.class))).thenReturn(menuItem);
        Mockito.when(menuItemMapper.outputCrudMapping(any(IMenuItem.class))).thenReturn(dtoCrudOutput);

        //test
        MenuItemDtoCrudOutput test = menuItemService.update(menuDtoInput, inputId, version);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getMenuId());
        Assertions.assertEquals(id, test.getPizzaInfoId());
        Assertions.assertEquals(price, test.getPrice());
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
        menuItemService.delete(inputId, version, delete);
        Mockito.verify(menuItemDao, times(1)).delete(valueId.capture(), valueVersion.capture(), valueDelete.capture());

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersion.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
    }
}