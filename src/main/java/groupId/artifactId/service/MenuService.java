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

    public MenuService(IMenuDao dao) {
        this.dao = dao;
    }

    @Override
    public List<MenuDtoOutput> get() {
        List<MenuDtoOutput> temp = new ArrayList<>();
        for (IMenu menu : this.dao.get()) {
            MenuDtoOutput menuDtoOutput = MenuMapper.menuOutputMapping(menu);
            temp.add(menuDtoOutput);
        }
        return temp;
    }

    @Override
    public MenuDtoOutput get(Long id) {
        return MenuMapper.menuOutputMapping(this.dao.get(id));
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
        IMenu menu = this.dao.update(MenuMapper.menuInputMapping(menuDtoInput), Long.valueOf(id), Integer.valueOf(version));
        return MenuMapper.menuOutputMapping(menu);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }

    @Override
    public MenuDtoOutput save(MenuDtoInput menuDtoInput) {
        IMenu menu = this.dao.save(MenuMapper.menuInputMapping(menuDtoInput));
        return MenuMapper.menuOutputMapping(menu);
    }

}
