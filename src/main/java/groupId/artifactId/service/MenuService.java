package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
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
    public List<MenuDtoCrudOutput> get() {
        List<MenuDtoCrudOutput> temp = new ArrayList<>();
        for (IMenu menu : this.dao.get()) {
            MenuDtoCrudOutput menuDtoOutput = menuMapper.outputCrudMapping(menu);
            temp.add(menuDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuDtoCrudOutput get(Long id) {
        return menuMapper.outputCrudMapping(this.dao.get(id));
    }

    @Override
    public MenuDtoOutput getAllData(Long id) {
        return menuMapper.outputMapping(this.dao.getAllData(id));
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
    public MenuDtoCrudOutput update(MenuDtoInput menuDtoInput, String id, String version) {
        IMenu menu = this.dao.update(menuMapper.inputMapping(menuDtoInput), Long.valueOf(id), Integer.valueOf(version));
        return menuMapper.outputCrudMapping(menu);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }

    @Override
    public MenuDtoCrudOutput save(MenuDtoInput menuDtoInput) {
        IMenu menu = this.dao.save(menuMapper.inputMapping(menuDtoInput));
        return menuMapper.outputCrudMapping(menu);
    }

}
