package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.exceptions.ServiceException;
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
        try {
            IMenuItem menuItem = this.dao.save(menuItemMapper.inputMapping(menuItemDtoInput));
            return menuItemMapper.outputCrudMapping(menuItem);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Menu item" + menuItemDtoInput, e);
        }
    }

    @Override
    public List<MenuItemDtoCrudOutput> get() {
        try {
            List<MenuItemDtoCrudOutput> temp = new ArrayList<>();
            for (IMenuItem menuItem : this.dao.get()) {
                MenuItemDtoCrudOutput menuItemDtoOutput = menuItemMapper.outputCrudMapping(menuItem);
                temp.add(menuItemDtoOutput);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Menu Item at Service", e);
        }
    }

    @Override
    public MenuItemDtoCrudOutput get(Long id) {
        try {
            return menuItemMapper.outputCrudMapping(this.dao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu Item at Service by id" + id, e);
        }
    }

    @Override
    public MenuItemDtoOutput getAllData(Long id) {
        try {
            return menuItemMapper.outputMapping(this.dao.getAllData(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Menu Item at Service by id" + id, e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        try {
            return this.dao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Menu Item at Service by id" + id, e);
        }
    }

    @Override
    public MenuItemDtoCrudOutput update(MenuItemDtoInput menuItemDtoInput, String id, String version) {
        try {
            IMenuItem menuItem = this.dao.update(menuItemMapper.inputMapping(menuItemDtoInput), Long.valueOf(id), Integer.valueOf(version));
            return menuItemMapper.outputCrudMapping(menuItem);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Menu item" + menuItemDtoInput + "by id:" + id, e);
        }
    }

    @Override
    public void delete(String id, String version, String delete) {
        try {
            this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Menu item with id:" + id, e);
        }
    }
}
