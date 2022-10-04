package groupId.artifactId.service;

import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.storage.api.IMenuStorage;
import groupId.artifactId.storage.api.StorageFactory;

import java.util.List;

public class MenuService implements IMenuService {
    private static MenuService firstInstance = null;
    private final IMenuStorage storage;

    private MenuService() {
        this.storage = StorageFactory.getInstance().getMenuStorage();
    }

    public static MenuService getInstance() {
        synchronized (MenuService.class) {
            if (firstInstance == null) {
                firstInstance = new MenuService();
            }
        }
        return firstInstance;
    }

    @Override
    public List<IMenu> get() {
        return this.storage.get();
    }

    @Override
    public void add(IMenu menu) {
//        this.validator.validate(productCreationDto);
        this.storage.save(ProductMapper.productMapping(productCreationDto));
    }
}
