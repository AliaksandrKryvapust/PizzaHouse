package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.List;

public class MenuItemDao implements IMenuItemDao {
    private static MenuItemDao firstInstance = null;
    private final DataSource dataSource;
    public MenuItemDao() {
        try {
            this.dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class at MenuItemDao", e);
        }
    }
    public static MenuItemDao getInstance() {
        synchronized (MenuItemDao.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemDao();
            }
        }
        return firstInstance;
    }
    @Override
    public void save(IMenuItem iMenuItem) throws SQLException {

    }

    @Override
    public void update(IMenuItem iMenuItem) throws SQLException {

    }

    @Override
    public IMenuItem get(Long id) throws SQLException {
        return null;
    }

    @Override
    public void delete(Long id, Integer version) throws SQLException {

    }

    @Override
    public List<MenuItem> get() {
        return null;
    }

    @Override
    public Boolean isIdExist(Long id) {
        return null;
    }
}
