package groupId.artifactId.storage.api;

public interface IStorageFactory {
    IMenuStorage getMenuStorage();
    ITokenStorage getTokenStorage();
    IOrderDataStorage getOrderDataStorage();
}
