package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.*;

@Repository
public class MenuItemDao implements IMenuItemDao {
    private static final String SELECT_MENU_ITEM = "SELECT menuItem from MenuItem menuItem ORDER BY id";
    private static final String SELECT_MENU_ITEMS_BY_IDS = "SELECT menuItem FROM MenuItem menuItem WHERE menuItem.id IN :ids";
    @PersistenceContext
    private final EntityManager entityManager;

    public MenuItemDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public IMenuItem save(IMenuItem menuItem, EntityManager entityTransaction) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try {
            entityTransaction.persist(menuItem);
            return menuItem;
        } catch (PersistenceException e) {
            if (e.getMessage().contains(MENU_ITEM_UK) || e.getMessage().contains(MENU_ITEM_FK)
                    || e.getMessage().contains(MENU_ITEM_FK2)) {
                throw new NoContentException("menu item table insert failed,  check preconditions and FK values: "
                        + menuItem);
            } else {
                throw new DaoException("Failed to save new MenuItem" + menuItem + "\t cause" + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to save new MenuItem" + menuItem + "\t cause" + e.getMessage(), e);
        }
    }

    @Override
    public IMenuItem update(IMenuItem menuItem, Long id, Integer version, EntityManager entityTransaction) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try {
            MenuItem currentEntity = (MenuItem) this.getLock(id, entityTransaction);
            if (!currentEntity.getVersion().equals(version)) {
                throw new OptimisticLockException();
            }
            PizzaInfo currentPizzaInfo = currentEntity.getPizzaInfo();
            currentPizzaInfo.setName(menuItem.getPizzaInfo().getName());
            currentPizzaInfo.setDescription(menuItem.getPizzaInfo().getDescription());
            currentPizzaInfo.setSize(menuItem.getPizzaInfo().getSize());
            currentEntity.setPrice(menuItem.getPrice());
            currentEntity.setPizzaInfo(currentPizzaInfo);
            entityTransaction.merge(currentEntity);
            return currentEntity;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
        } catch (PersistenceException e) {
            if (e.getMessage().contains(MENU_ITEM_UK) || e.getMessage().contains(MENU_ITEM_FK)
                    || e.getMessage().contains(MENU_ITEM_FK2)) {
                throw new NoContentException("menu_item table insert failed,  check preconditions and FK values: "
                        + menuItem);
            } else {
                throw new DaoException("Failed to update menu_item" + menuItem + " by id:" + id + "\t cause" + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Failed to update menu_item" + menuItem + " by id:" + id + "\t cause" + e.getMessage(), e);
        }
    }

    @Override
    public IMenuItem get(Long id) {
        try {
            MenuItem menuItem = entityManager.find(MenuItem.class, id);
            if (menuItem == null) {
                throw new NoContentException("There is no Menu Item with id:" + id);
            }
            return menuItem;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Menu Item from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id, Boolean delete, EntityManager entityTransaction) {
        try {
            MenuItem menuItem = (MenuItem) this.getLock(id, entityTransaction);
            if (delete){
                entityTransaction.remove(menuItem);
            }
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to delete Menu Item with id:" + id + "\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IMenuItem> get() {
        try {
            List<?> iMenuItems = entityManager.createQuery(SELECT_MENU_ITEM).getResultList();
            List<IMenuItem> output = iMenuItems.stream().filter((i) -> i instanceof IMenuItem)
                    .map(IMenuItem.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of menu Item");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of menu Item\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public IMenuItem getLock(Long id, EntityManager entityTransaction) {
        try {
            MenuItem menuItem = entityTransaction.find(MenuItem.class, id);
            if (menuItem == null) {
                throw new NoContentException("There is no Menu Item with id:" + id);
            }
            return menuItem;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Lock of Menu Item from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IMenuItem> getAllLock(List<Long> ids, EntityManager entityTransaction) {
        try {
            List<?> iMenuItems = entityTransaction.createQuery(SELECT_MENU_ITEMS_BY_IDS)
                    .setParameter("ids", ids).getResultList();
            List<IMenuItem> output = iMenuItems.stream().filter((i) -> i instanceof IMenuItem)
                    .map(IMenuItem.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of menu Item");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of menu Item\tcause: " + e.getMessage(), e);
        }
    }
}
