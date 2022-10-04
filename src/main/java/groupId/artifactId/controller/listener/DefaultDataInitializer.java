package groupId.artifactId.controller.listener;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.PizzaInfo;
import groupId.artifactId.storage.entity.api.IMenuItem;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.service.MenuService;
import groupId.artifactId.service.api.IMenuService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

public class DefaultDataInitializer implements ServletContextListener {
    IMenuService menuService = MenuService.getInstance();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<MenuItemDto> temp = new ArrayList<>();
        temp.add(new MenuItemDto(new PizzaInfo("MARGHERITA PIZZA","Mozzarella cheese, herb tomato sauce",
                32),15.0));
        temp.add(new MenuItemDto(new PizzaInfo("PEPPERONI PIZZA","Pepperoni sausage, mozzarella cheese, herb tomato sauce",
                32),18.0));
        temp.add(new MenuItemDto(new PizzaInfo("CLASSICA PIZZA","Ham, mushrooms, mozzarella cheese, herb tomato sauce",
                32),19.0));
        temp.add(new MenuItemDto(new PizzaInfo("AMERICANA PIZZA","Mushrooms, tomatoes, mozzarella cheese, herb tomato sauce",
                32),18.0));
        menuService.add(temp);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
