package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.SelectedItemDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.ISelectedItemDao;

import java.beans.PropertyVetoException;

public class SelectedItemDaoSingleton {
    private final ISelectedItemDao selectedItemDao;
    private volatile static SelectedItemDaoSingleton instance = null;

    public SelectedItemDaoSingleton() {
        try {
            this.selectedItemDao = new SelectedItemDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for SelectedItemDao", e);
        }
    }

    public static ISelectedItemDao getInstance() {
        synchronized (SelectedItemDaoSingleton.class) {
            if (instance == null) {
                instance = new SelectedItemDaoSingleton();
            }
        }
        return instance.selectedItemDao;
    }
}
