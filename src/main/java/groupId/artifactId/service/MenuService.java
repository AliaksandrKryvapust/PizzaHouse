package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;

import java.util.ArrayList;
import java.util.List;

public class MenuService implements IMenuService {
    private static MenuService firstInstance = null;
    private final IMenuDao dao;

    private MenuService() {
        this.dao = MenuDao.getInstance();
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
    public List<MenuDtoOutput> get() {
        List<MenuDtoOutput> temp = new ArrayList<>();
        for (IMenu menu: this.dao.get()) {
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
    public void update(MenuDtoInput menuDtoInput, String id, String version) {
        this.dao.update(MenuMapper.menuInputMapping(menuDtoInput), Long.valueOf(id), Integer.valueOf(version));
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }

    @Override
    public void save(MenuDtoInput menuDtoInput) {
        this.dao.save(MenuMapper.menuInputMapping(menuDtoInput));
    }

}
