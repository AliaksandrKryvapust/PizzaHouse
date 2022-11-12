package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.service.api.IMenuItemService;

import java.util.ArrayList;
import java.util.List;

public class MenuItemService implements IMenuItemService {
    private final IMenuItemDao dao;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(IMenuItemDao dao, MenuItemMapper menuItemMapper) {
        this.dao = dao;
        this. menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDtoOutput save(MenuItemDtoInput menuItemDtoInput) {
        IMenuItem menuItem = this.dao.save(menuItemMapper.menuItemInputMapping(menuItemDtoInput));
        return menuItemMapper.menuItemOutputMapping(menuItem);
    }

    @Override
    public List<MenuItemDtoOutput> get() {
        List<MenuItemDtoOutput> temp = new ArrayList<>();
        for (IMenuItem menuItem : this.dao.get()) {
            MenuItemDtoOutput menuItemDtoOutput = menuItemMapper.menuItemOutputMapping(menuItem);
            temp.add(menuItemDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuItemDtoOutput get(Long id) {
        return menuItemMapper.menuItemOutputMapping(this.dao.get(id));
    }

    @Override
    public MenuItemDtoOutput getAllData(Long id) {
        return menuItemMapper.menuItemOutputMapping(this.dao.getAllData(id));
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.exist(id);
    }

    @Override
    public MenuItemDtoOutput update(MenuItemDtoInput menuItemDtoInput, String id, String version) {
        IMenuItem menuItem = this.dao.update(menuItemMapper.menuItemInputMapping(menuItemDtoInput), Long.valueOf(id), Integer.valueOf(version));
        return menuItemMapper.menuItemOutputMapping(menuItem);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }
}
