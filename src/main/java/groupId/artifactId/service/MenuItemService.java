package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
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
    public MenuItemDtoCrudOutput save(MenuItemDtoInput menuItemDtoInput) {
        IMenuItem menuItem = this.dao.save(menuItemMapper.inputMapping(menuItemDtoInput));
        return menuItemMapper.outputCrudMapping(menuItem);
    }

    @Override
    public List<MenuItemDtoCrudOutput> get() {
        List<MenuItemDtoCrudOutput> temp = new ArrayList<>();
        for (IMenuItem menuItem : this.dao.get()) {
            MenuItemDtoCrudOutput menuItemDtoOutput = menuItemMapper.outputCrudMapping(menuItem);
            temp.add(menuItemDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuItemDtoCrudOutput get(Long id) {
        return menuItemMapper.outputCrudMapping(this.dao.get(id));
    }

    @Override
    public MenuItemDtoOutput getAllData(Long id) {
        return menuItemMapper.outputMapping(this.dao.getAllData(id));
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.exist(id);
    }

    @Override
    public MenuItemDtoCrudOutput update(MenuItemDtoInput menuItemDtoInput, String id, String version) {
        IMenuItem menuItem = this.dao.update(menuItemMapper.inputMapping(menuItemDtoInput), Long.valueOf(id), Integer.valueOf(version));
        return menuItemMapper.outputCrudMapping(menuItem);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }
}
