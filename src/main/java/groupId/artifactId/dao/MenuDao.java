package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.MENU_UK;


public class MenuDao implements IMenuDao {
    private static final String SELECT_MENU = "SELECT menu from Menu menu ORDER BY id";
    private final EntityManager entityManager;

    public MenuDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<IMenu> get() {
        try {
            List<?> iMenu = entityManager.createQuery(SELECT_MENU).getResultList();
            List<IMenu> output = iMenu.stream().filter((i) -> i instanceof IMenu)
                    .map(IMenu.class::cast).collect(Collectors.toList());
            if (!output.isEmpty()) {
                return output;
            } else {
                throw new IllegalStateException("Failed to get List of menu");
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of menu\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public IMenu get(Long id) {
        try {
            Menu menu = entityManager.find(Menu.class, id);
            if (menu == null) {
                throw new NoContentException("There is no Menu with id:" + id);
            }
            return menu;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Menu from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }

    @Override
    public IMenu save(IMenu menu, EntityManager entityTransaction) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        try {
            entityTransaction.persist(menu);
            return menu;
        } catch (Exception e) {
            if (e.getMessage().contains(MENU_UK)) {
                throw new NoContentException("menu table insert failed,  check preconditions and FK values: "
                        + menu);
            } else {
                throw new DaoException("Failed to save new Menu" + menu + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public IMenu update(IMenu menu, Long id, Integer version, EntityManager entityTransaction) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        try {
            Menu currentEntity = (Menu) this.getLock(id, entityTransaction);
            if (!currentEntity.getVersion().equals(version)) {
                throw new OptimisticLockException();
            }
            currentEntity.setName(menu.getName());
            currentEntity.setEnable(menu.getEnable());
            if (menu.getItems() != null && !menu.getItems().isEmpty()) {
                currentEntity.setItems(menu.getItems());
            }
            entityTransaction.merge(currentEntity);
            return currentEntity;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("menu table update failed, version does not match update denied");
        } catch (Exception e) {
            if (e.getMessage().contains(MENU_UK)) {
                throw new NoContentException("menu table update failed,  check preconditions and FK values: "
                        + menu);
            } else {
                throw new DaoException("Failed to update menu" + menu + " by id:" + id + "\t cause" + e.getMessage(), e);
            }
        }
    }

    @Override
    public IMenu updateItems(IMenu menu, IMenuItem menuItem, EntityManager entityTransaction) {
        try {
            List<IMenuItem> items = menu.getItems();
            items.add(menuItem);
            Menu currentEntity = (Menu) menu;
            currentEntity.setItems(items);
            entityTransaction.merge(currentEntity);
            return currentEntity;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("menu table update failed, version does not match update denied");
        } catch (Exception e) {
            if (e.getMessage().contains(MENU_UK)) {
                throw new NoContentException("menu table update failed,  check preconditions and FK values: "
                        + menu);
            } else {
                throw new DaoException("Failed to update menu" + menu + " with MenuItem:" + menuItem + "\t cause" +
                        e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(Long id, Boolean delete, EntityManager entityTransaction) {
        try {
            Menu menu = (Menu) this.getLock(id, entityTransaction);
            if (delete) {
                entityTransaction.remove(menu);
            } else {
                menu.setEnable(false);
                entityTransaction.merge(menu);
            }
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to delete Menu with id:" + id + "\tcause: " + e.getMessage(), e);
        }
    }

    @Override
    public IMenu getLock(Long id, EntityManager entityTransaction) {
        try {
            Menu menu = entityTransaction.find(Menu.class, id);
            if (menu == null) {
                throw new NoContentException("There is no Menu with id:" + id);
            }
            return menu;
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new DaoException("Failed to get Lock of Menu from Data Base by id:" + id + "cause: " + e.getMessage(), e);
        }
    }
}
