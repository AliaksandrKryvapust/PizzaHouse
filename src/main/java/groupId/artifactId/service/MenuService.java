package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.exceptions.IncorrectSQLConnectionException;
import groupId.artifactId.service.api.IMenuValidator;
import groupId.artifactId.service.api.IMenuService;

import java.sql.SQLException;
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
    public List<Menu> get() {
        return this.dao.get();
    }

    @Override
    public IMenu get(Long id) {
        try {
            return this.dao.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Menu with id " + id,e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.isIdExist(id);
    }

    @Override
    public Boolean isDishExist(String name) {
        return this.dao.isDishExist(name);
    }

    @Override
    public void add(List<MenuItemDto> menuItemDto) {
        this.validator.validateMenu(menuItemDto);
        try {
            this.dao.save(MenuMapper.menuMapping(menuItemDto));
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to save new Menu", e);
        }
    }

    @Override
    public void addMenuItem(MenuItemDtoWithId menuItemDtoWithId) {
        this.validator.validateMenuItem(menuItemDtoWithId);
        this.dao.add(MenuMapper.menuItemWithIdMapping(menuItemDtoWithId), menuItemDtoWithId.getId());
    }
}
