package groupId.artifactId.service;

import groupId.artifactId.controller.validator.MenuItemValidator;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;

import java.util.List;

public class MenuItemService implements IMenuItemService {
    private static MenuItemService firstInstance = null;
    private final IMenuItemDao dao;
    private final IMenuItemValidator validator;

    private MenuItemService() {
        this.dao = MenuItemDao.getInstance();
        this.validator = MenuItemValidator.getInstance();
    }

    public static MenuItemService getInstance() {
        synchronized (MenuItemService.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemService();
            }
        }
        return firstInstance;
    }

    @Override
    public void save(MenuItemDto menuItemDto) {
        this.validator.validateMenuItem(menuItemDto);
        this.dao.save(MenuMapper.menuItemMapping(menuItemDto));
    }

    @Override
    public List<IMenuItem> get() {
        return this.dao.get();
    }

    @Override
    public IMenuItem get(Long id) {
        return this.dao.get(id);
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.isIdExist(id);
    }

    @Override
    public void update(MenuItemDto menuItemDto) {
        this.validator.validateMenuItem(menuItemDto);
        this.dao.update(MenuMapper.menuItemMapping(menuItemDto));
    }

    @Override
    public void delete(String id, String version) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version));
    }
}
