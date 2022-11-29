package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IMenuService;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuItemService implements IMenuItemService {
    private final IMenuItemDao menuItemDao;
    private final MenuItemMapper menuItemMapper;
    private final EntityManager entityManager;

    private final IMenuService menuService;

    public MenuItemService(IMenuItemDao menuItemDao, MenuItemMapper menuItemMapper, EntityManager entityManager, IMenuService menuService) {
        this.menuItemDao = menuItemDao;
        this.menuItemMapper = menuItemMapper;
        this.entityManager = entityManager;
        this.menuService = menuService;
    }

    @Override
    public MenuItemDtoOutput save(MenuItemDtoInput menuItemDtoInput) {
        try {
            entityManager.getTransaction().begin();
            IMenuItem menuItem = this.menuItemDao.save(menuItemMapper.inputMapping(menuItemDtoInput), this.entityManager);
            if (menuItemDtoInput.getMenuId() != null) {
                IMenu menu = menuService.getRow(menuItemDtoInput.getMenuId(), this.entityManager);
                List<IMenuItem> items = menuService.updateItem(menu, menuItem, this.entityManager).getItems();
                entityManager.getTransaction().commit();
                return menuItemMapper.outputMapping(Objects.requireNonNull(items.stream()
                        .filter((i) -> i.getPizzaInfo().getName().equals(menuItemDtoInput.getPizzaInfoDtoInput().getName()))
                        .findFirst().orElse(new MenuItem())));
            } else {
                entityManager.getTransaction().commit();
                return menuItemMapper.outputMapping(menuItem);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to save Menu Item at Service" + menuItemDtoInput + "\tcause:"
                    + e.getMessage(), e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
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
    public List<IMenuItem> getRow(List<Long> ids, EntityManager entityTransaction) {
        try {
            return this.menuItemDao.getAllLock(ids, entityTransaction);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu Item at Service by id" + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuItemDtoOutput update(MenuItemDtoInput menuItemDtoInput, String id, String version) {
        try {
            entityManager.getTransaction().begin();
            IMenuItem menuItem = this.menuItemDao.update(menuItemMapper.inputMapping(menuItemDtoInput),
                    Long.valueOf(id), Integer.valueOf(version), this.entityManager);
            if (menuItemDtoInput.getMenuId() != null) {
                IMenu menu = menuService.getRow(menuItemDtoInput.getMenuId(), this.entityManager);
                List<IMenuItem> items = menuService.updateItem(menu, menuItem, this.entityManager).getItems();
                entityManager.getTransaction().commit();
                return menuItemMapper.outputMapping(Objects.requireNonNull(items.stream()
                        .filter((i) -> i.getPizzaInfo().getName().equals(menuItemDtoInput.getPizzaInfoDtoInput().getName()))
                        .findFirst().orElse(new MenuItem())));
            } else {
                entityManager.getTransaction().commit();
                return menuItemMapper.outputMapping(menuItem);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Menu Item at Service " + menuItemDtoInput + "by id:" + id
                    + "\tcause" + e.getMessage(), e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            entityManager.getTransaction().begin();
            this.menuItemDao.delete(Long.valueOf(id), Boolean.valueOf(delete), this.entityManager);
            entityManager.getTransaction().commit();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Menu Item with id:" + id, e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}
