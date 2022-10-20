package groupId.artifactId.dao.api;

import java.io.Serializable;
import java.sql.SQLException;

public interface IDao<TYPE> {
    void save(TYPE type) throws SQLException;
    TYPE get(Serializable id) throws SQLException;
    void delete(Long id) throws SQLException;
}
