package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IMenuItemService;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemService implements IMenuItemService {
    private final IMenuItemDao menuItemDao;
    private final MenuItemMapper menuItemMapper;
    private final EntityManager entityManager;

    public MenuItemService(IMenuItemDao menuItemDao, MenuItemMapper menuItemMapper, EntityManager entityManager) {
        this.menuItemDao = menuItemDao;
        this.menuItemMapper = menuItemMapper;
        this.entityManager = entityManager;
    }

    @Override
    public MenuItemDtoOutput save(MenuItemDtoInput menuItemDtoInput) {
        try {
            entityManager.getTransaction().begin();
            IMenuItem menuItem = this.menuItemDao.save(menuItemMapper.inputMapping(menuItemDtoInput), this.entityManager);
            entityManager.getTransaction().commit();
            return menuItemMapper.outputMapping(menuItem);
        } catch (DaoException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException("Failed to save Menu Item at Service" + menuItemDtoInput + "\tcause:"
                    + e.getMessage(), e);
        }
    }

    @Override
    public List<MenuItemDtoOutput> get() {
        try {
            List<MenuItemDtoOutput> temp = new ArrayList<>();
            for (IMenuItem menuItem : this.menuItemDao.get()) {
                MenuItemDtoOutput dtoCrudOutput = menuItemMapper.outputMapping(menuItem);
                temp.add(dtoCrudOutput);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Menu Item`s at Service\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuItemDtoOutput get(Long id) {
        try {
            IMenuItem menuItem = this.menuItemDao.get(id);
            return menuItemMapper.outputMapping(menuItem);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu Item at Service by id" + id + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuItemDtoCrudOutput update(MenuItemDtoInput menuItemDtoInput, String id, String version) {
        try {
            entityManager.getTransaction().begin();
            IMenuItem menuItem = this.menuItemDao.update(menuItemMapper.inputMapping(menuItemDtoInput),
                    Long.valueOf(id), Integer.valueOf(version));
            entityManager.getTransaction().commit();
            return menuItemMapper.outputCrudMapping(menuItem);
        } catch (DaoException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException(e.getMessage(), e);
        } catch (OptimisticLockException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new OptimisticLockException(e.getMessage());
        } catch (NoContentException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException("Failed to update Menu Item at Service " + menuItemDtoInput + "by id:" + id
                    + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            entityManager.getTransaction().begin();
            this.menuItemDao.delete(Long.valueOf(id), Boolean.valueOf(delete));
            entityManager.getTransaction().commit();
        } catch (DaoException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServiceException("Failed to delete Menu Item with id:" + id, e);
        }
    }
}
