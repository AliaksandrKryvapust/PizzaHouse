package groupId.artifactId.dao.test;

import groupId.artifactId.dao.DataSourceCreator;


import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        DataSource dataSource;
        try {
            dataSource = DataSourceCreator.getInstance();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT id, name, job, department\n"
                        + "FROM structure.employ;")) {
                    List<Employ> list = mapList(resultSet);
                    System.out.println(list);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Employ map(ResultSet resultSet) throws SQLException {
        Employ employ = new Employ();
        while (resultSet.next()) {
            employ.setId(resultSet.getLong("id"));
            employ.setName(resultSet.getString("name"));
            long departmentTemp = resultSet.getLong("department");
            if (!resultSet.wasNull()) {
                employ.setDepartmentId(departmentTemp);
            }
            long jobTemp = resultSet.getLong("job");
            if (!resultSet.wasNull()) {
                employ.setJobId(jobTemp);
            }
        }
        return employ;
    }
    public static List<Employ> mapList(ResultSet resultSet) throws SQLException {
        List<Employ> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(map(resultSet));
        }
        return list;
    }

}
