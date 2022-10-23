package groupId.artifactId.dao.test;

import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public class DaoTest {
    public static void main(String[] args) {
        MenuDao menuDao = MenuDao.getInstance();
        List<Menu> list = menuDao.get();
        System.out.println(list);
        System.out.println();
        System.out.println();
        System.out.println(menuDao.get(1L));
        menuDao.delete(1L,6);
    }

}
