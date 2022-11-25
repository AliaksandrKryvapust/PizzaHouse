package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IMenuService;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;

public class MenuService implements IMenuService {
    private final IMenuDao dao;
    private final MenuMapper menuMapper;
    private final EntityManager entityManager;

    public MenuService(IMenuDao dao, MenuMapper menuMapper, EntityManager entityManager) {
        this.dao = dao;
        this.menuMapper = menuMapper;
        this.entityManager = entityManager;
    }

    @Override
    public List<MenuDtoCrudOutput> get() {
        try {
            List<MenuDtoCrudOutput> temp = new ArrayList<>();
            for (IMenu menu : this.dao.get()) {
                MenuDtoCrudOutput dtoCrudOutput = menuMapper.outputCrudMapping(menu);
                temp.add(dtoCrudOutput);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Menu`s at Service\tcause" + e.getMessage(), e);
        }
    }


    @Override
    public IMenu getRow(Long id, EntityManager entityTransaction) {
        try {
            return this.dao.getLock(id, entityTransaction);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Menu`s at Service\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuDtoCrudOutput get(Long id) {
        try {
            IMenu menu = this.dao.get(id);
            return menuMapper.outputCrudMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu at Service by id" + id + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuDtoOutput getAllData(Long id) {
        try {
            IMenu menu = this.dao.get(id);
            return menuMapper.outputMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Menu at Service by id" + id + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public MenuDtoCrudOutput update(MenuDtoInput menuDtoInput, String id, String version) {
        try {
            entityManager.getTransaction().begin();
            IMenu menu = this.dao.update(menuMapper.inputMapping(menuDtoInput),
                    Long.valueOf(id), Integer.valueOf(version), this.entityManager);
            entityManager.getTransaction().commit();
            return menuMapper.outputCrudMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (javax.persistence.OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Menu at Service " + menuDtoInput + "by id:" + id
                    + "\tcause" + e.getMessage(), e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public IMenu updateItem(IMenu menu, IMenuItem menuItem, EntityManager entityTransaction) {
        try {
            return this.dao.updateItems(menu, menuItem, entityTransaction);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (javax.persistence.OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Menu at Service " + menu + "by menuItem:" + menuItem
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
            this.dao.delete(Long.valueOf(id), Boolean.valueOf(delete), this.entityManager);
            entityManager.getTransaction().commit();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Menu with id:" + id, e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public MenuDtoCrudOutput save(MenuDtoInput menuDtoInput) {
        try {
            entityManager.getTransaction().begin();
            IMenu menu = this.dao.save(menuMapper.inputMapping(menuDtoInput), this.entityManager);
            entityManager.getTransaction().commit();
            return menuMapper.outputCrudMapping(menu);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to save Menu at Service" + menuDtoInput + "\tcause:"
                    + e.getMessage(), e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}
