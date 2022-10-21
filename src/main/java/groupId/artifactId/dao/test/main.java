package groupId.artifactId.dao.test;

import groupId.artifactId.dao.DataSourceCreator;
import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.entity.api.IMenu;


import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        MenuDao menuDao = MenuDao.getInstance();
        List<IMenu> list = menuDao.get();
        System.out.println(list);
    }

}
