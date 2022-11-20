package groupId.artifactId.dao.api;

public interface IDaoUpdate<TYPE> {
    TYPE update(TYPE type, Long id, Integer version);
}
