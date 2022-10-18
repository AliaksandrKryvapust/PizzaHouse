package groupId.artifactId.dao.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class main1 {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Company",
                "postgres", "postgres")) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT id, name, job, department\n" + "FROM structure.employ;")) {
                    List<Employ> list = new ArrayList<>();
                    while (resultSet.next()){
                        Employ employ = new Employ();
                        employ.setId(resultSet.getLong("id"));
                        employ.setName(resultSet.getString("name"));
                        long depRaw = employ.setJobId(resultSet.getLong("job"));
                        employ.setDepartmentId(resultSet.getLong("department"));
                        if (!resultSet.wasNull()){
                            employ.setJobId();
                        }
                        list.add(employ);
                    }
                    System.out.println(list);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
