package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;

import java.util.ArrayList;
import java.util.List;

public class MenuService implements IMenuService {
    private final IMenuDao dao;
    private final MenuMapper menuMapper;

    public MenuService(IMenuDao dao, MenuMapper menuMapper) {
        this.dao = dao;
        this.menuMapper = menuMapper;
    }

    @Override
    public List<MenuDtoOutput> get() {
        List<MenuDtoOutput> temp = new ArrayList<>();
        for (IMenu menu : this.dao.get()) {
            MenuDtoOutput menuDtoOutput = menuMapper.menuOutputMapping(menu);
            temp.add(menuDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuDtoOutput get(Long id) {
        return menuMapper.menuOutputMapping(this.dao.get(id));
    }

    @Override
    public MenuDtoOutput getAllData(Long id) {
        return menuMapper.menuOutputMapping(this.dao.getAllData(id));
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
    public MenuDtoOutput update(MenuDtoInput menuDtoInput, String id, String version) {
        IMenu menu = this.dao.update(menuMapper.menuInputMapping(menuDtoInput), Long.valueOf(id), Integer.valueOf(version));
        return menuMapper.menuOutputMapping(menu);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }

    @Override
    public MenuDtoOutput save(MenuDtoInput menuDtoInput) {
        IMenu menu = this.dao.save(menuMapper.menuInputMapping(menuDtoInput));
        return menuMapper.menuOutputMapping(menu);
    }

}
