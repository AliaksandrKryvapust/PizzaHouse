package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.service.api.IMenuValidator;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.storage.api.IMenuStorage;
import groupId.artifactId.storage.api.StorageFactory;

import java.util.List;

public class MenuService implements IMenuService {
    private static MenuService firstInstance = null;
    private final IMenuStorage storage;
    private final IMenuValidator validator;

    private MenuService() {
        this.storage = StorageFactory.getInstance().getMenuStorage();
        this.validator=MenuValidator.getInstance();
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
        return this.storage.get();
    }

    @Override
    public Boolean isIdValid(int id) {
        return this.storage.isIdExist(id);
    }

    @Override
    public void add(List<MenuItemDto> menuItemDto) {
        this.validator.validateMenu(menuItemDto);
        this.storage.add(MenuMapper.menuMapping(menuItemDto));
    }

    @Override
    public void addMenuItem(MenuItemDtoWithId menuItemDtoWithId) {
        this.validator.validateMenuItem(menuItemDtoWithId);
        this.storage.addMenuItem(MenuMapper.menuItemWithIdMapping(menuItemDtoWithId), menuItemDtoWithId.getId());
    }
}
