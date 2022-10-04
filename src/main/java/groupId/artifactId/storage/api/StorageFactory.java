package groupId.artifactId.storage.api;

import groupId.artifactId.storage.MenuMemoryStorage;

public class StorageFactory implements IStorageFactory{
    private static StorageFactory firstInstance = null;
    private final IMenuStorage menuStorage;
    private StorageFactory(){
        this.menuStorage= new MenuMemoryStorage();
    }

    public static StorageFactory getInstance() {
        synchronized (StorageFactory.class) {
            if (firstInstance == null) {
                firstInstance = new StorageFactory();
            }
        }
        return firstInstance;
    }

    @Override
    public IMenuStorage getMenuStorage() {
        return menuStorage;
    }
}
