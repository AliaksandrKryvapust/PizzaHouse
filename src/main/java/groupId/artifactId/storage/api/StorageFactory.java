package groupId.artifactId.storage.api;

import groupId.artifactId.storage.MenuMemoryStorage;
import groupId.artifactId.storage.OrderDataMemoryStorage;
import groupId.artifactId.storage.TokenMemoryStorage;

public class StorageFactory implements IStorageFactory {
    private static StorageFactory firstInstance = null;
    private final IMenuStorage menuStorage;
    private final ITokenStorage tokenStorage;
    private final IOrderDataStorage orderDataStorage;

    private StorageFactory() {
        this.menuStorage = new MenuMemoryStorage();
        this.tokenStorage = new TokenMemoryStorage();
        this.orderDataStorage = new OrderDataMemoryStorage();
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
        return this.menuStorage;
    }

    @Override
    public ITokenStorage getTokenStorage() {
        return this.tokenStorage;
    }

    @Override
    public IOrderDataStorage getOrderDataStorage() {
        return this.orderDataStorage;
    }
}
