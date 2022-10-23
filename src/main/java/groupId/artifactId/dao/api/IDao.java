package groupId.artifactId.dao.api;

import java.sql.SQLException;

public interface IDao<TYPE> {
    void save(TYPE type) throws SQLException;
    void update(TYPE type) throws SQLException;
    TYPE get(Long id) throws SQLException;
    void delete(Long id, Integer version) throws SQLException;
}
