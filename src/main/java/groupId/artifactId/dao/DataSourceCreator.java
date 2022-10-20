package groupId.artifactId.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class DataSourceCreator {

    private static DataSourceCreator firstInstance = null;
    private final ComboPooledDataSource dataSource;

    private DataSourceCreator() throws PropertyVetoException {
        dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/pizzeria");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(5);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(100);
    }

    public static DataSource getInstance() throws PropertyVetoException {
        synchronized (DataSourceCreator.class) {
            if (firstInstance == null) {
                firstInstance = new DataSourceCreator();
            }
        }
        return firstInstance.dataSource;
    }

}
