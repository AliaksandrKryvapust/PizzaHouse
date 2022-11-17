package groupId.artifactId.dao.test;

import groupId.artifactId.dao.IoC.MenuDaoSingleton;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public class DaoTest {
    public static void main(String[] args) {
        IMenuDao menuDao = MenuDaoSingleton.getInstance();
        List<IMenu> list = menuDao.get();
        System.out.println(list);
        System.out.println();
        System.out.println();
        System.out.println(menuDao.get(1L));
        menuDao.delete(1L, false);
    }

}
