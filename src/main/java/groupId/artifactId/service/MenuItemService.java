package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.service.api.IMenuItemValidator;
import groupId.artifactId.service.api.IMenuItemService;

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

    }

    @Override
    public List<MenuItem> get() {
        return null;
    }

    @Override
    public IMenuItem get(Long id) {
        return null;
    }

    @Override
    public Boolean isIdValid(Long id) {
        return null;
    }

    @Override
    public void update(MenuItemDto menuItemDto) {

    }

    @Override
    public void delete(String id, String version) {

    }
}
