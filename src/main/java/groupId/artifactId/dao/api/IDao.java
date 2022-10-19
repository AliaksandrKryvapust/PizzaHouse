package groupId.artifactId.dao.api;

import java.io.Serializable;
import java.sql.SQLException;

public interface IDao<TYPE> {
    TYPE save(TYPE type) throws SQLException;
    TYPE get(Serializable id) throws SQLException;
    void update(TYPE type) throws SQLException;
    int delete(Serializable id) throws SQLException;
}
