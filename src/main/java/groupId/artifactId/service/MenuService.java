package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
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
    public void update(MenuDto menuDto) {
        this.validator.validateMenu(menuDto);
        try {
            this.dao.update(MenuMapper.menuMapping(menuDto));
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to update Menu", e);
        }
    }

    @Override
    public void delete(String id, String version) {
        try {
            this.dao.delete(Long.valueOf(id),Integer.valueOf(version));
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to delete Menu", e);
        }
    }

    @Override
    public void save(List<MenuItemDto> menuItemDto) {
        this.validator.validateListMenuItems(menuItemDto);
        try {
            this.dao.save(MenuMapper.menuItemsMapping(menuItemDto));
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Failed to save new Menu", e);
        }
    }

    @Override
    public void addMenuItem(MenuItemDto menuItemDto) {
        this.validator.validateMenuItem(menuItemDto);
        this.dao.add(MenuMapper.menuItemWithIdMapping(menuItemDto), menuItemDto.getId());
    }
}
