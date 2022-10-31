package groupId.artifactId.service;

import groupId.artifactId.controller.validator.MenuValidator;
import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.controller.validator.api.IMenuValidator;

import java.util.ArrayList;
import java.util.List;

public class MenuService implements IMenuService {
    private static MenuService firstInstance = null;
    private final IMenuDao dao;
    private final IMenuValidator validator;

    private MenuService() {
        this.dao = MenuDao.getInstance();
        this.validator = MenuValidator.getInstance();
    }

    public static MenuService getInstance() {
        synchronized (MenuService.class) {
            if (firstInstance == null) {
                firstInstance = new MenuService();
            }
        }
        return firstInstance;
    }

    @Override
    public List<MenuDtoOutput> get() {
        List<MenuDtoOutput> temp = new ArrayList<>();
        for (IMenu menu: this.dao.get()) {
            MenuDtoOutput menuDtoOutput = MenuMapper.menuOutputMapping(menu);
            temp.add(menuDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuDtoOutput get(Long id) {
        return MenuMapper.menuOutputMapping(this.dao.get(id));
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.exist(id);
    }

    @Override
    public Boolean exist(String name) {
        return this.dao.doesMenuExist(name);
    }

    @Override
    public void update(MenuDtoInput menuDtoInput) {
        this.validator.validateMenu(menuDtoInput);
        this.dao.update(MenuMapper.menuInputMapping(menuDtoInput));
    }

    @Override
    public void delete(String id, String version) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version));
    }

    @Override
    public void save(MenuDtoInput menuDtoInput) {
        this.dao.save(MenuMapper.menuInputMapping(menuDtoInput));
    }

    @Override
    public void addMenuItem(MenuItemDto menuItemDto) {
        this.validator.validateMenuItem(menuItemDto);
        this.dao.add(MenuMapper.menuItemMapping(menuItemDto), menuItemDto.getId());
    }
}
