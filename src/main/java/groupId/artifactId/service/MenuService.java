package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.exceptions.ServiceException;
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
        try {
            List<MenuDtoCrudOutput> temp = new ArrayList<>();
            for (IMenu menu : this.dao.get()) {
                MenuDtoCrudOutput menuDtoOutput = menuMapper.outputCrudMapping(menu);
                temp.add(menuDtoOutput);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Menu at Service", e);
        }
    }

    @Override
    public MenuDtoCrudOutput get(Long id) {
        try {
            return menuMapper.outputCrudMapping(this.dao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu at Service by id" + id, e);
        }
    }

    @Override
    public MenuDtoOutput getAllData(Long id) {
        try {
            return menuMapper.outputMapping(this.dao.getAllData(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Menu at Service by id" + id, e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        try {
            return this.dao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Menu at Service by id " + id, e);
        }
    }

    @Override
    public Boolean exist(String name) {
        try {
            return this.dao.doesMenuExist(name);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Menu at Service by name: " + name, e);
        }
    }

    @Override
    public MenuDtoCrudOutput update(MenuDtoInput menuDtoInput, String id, String version) {
        if (!isIdValid(Long.valueOf(id))) {
            throw new NoContentException("Menu Id is not valid");
        }
        try {
            IMenu menu = this.dao.update(menuMapper.inputMapping(menuDtoInput), Long.valueOf(id), Integer.valueOf(version), EntityManagerFactoryHibernate.getEntityManager());
            return menuMapper.outputCrudMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Menu " + menuDtoInput + "by id:" + id, e);
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            this.dao.delete(Long.valueOf(id), Boolean.valueOf(delete));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Menu with id:" + id, e);
        }
    }

    @Override
    public MenuDtoCrudOutput save(MenuDtoInput menuDtoInput) {
        try {
            IMenu menu = this.dao.save(menuMapper.inputMapping(menuDtoInput), EntityManagerFactoryHibernate.getEntityManager());
            return menuMapper.outputCrudMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Menu item" + menuDtoInput, e);
        }
    }

}
