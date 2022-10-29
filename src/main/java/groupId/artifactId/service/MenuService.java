package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.service.api.IMenuValidator;

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
    public List<IMenu> get() {
        return this.dao.get();
    }

    @Override
    public IMenu get(Long id) {
        return this.dao.get(id);
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
        this.dao.update(MenuMapper.menuMapping(menuDto));
    }

    @Override
    public void delete(String id, String version) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version));
    }

    @Override
    public void save(MenuDto menuDto) {
        this.validator.validateMenuRow(menuDto);
        this.dao.save(MenuMapper.menuMapping(menuDto));
    }

    @Override
    public void addMenuItem(MenuItemDto menuItemDto) {
        this.validator.validateMenuItem(menuItemDto);
        this.dao.add(MenuMapper.menuItemMapping(menuItemDto), menuItemDto.getId());
    }
}
