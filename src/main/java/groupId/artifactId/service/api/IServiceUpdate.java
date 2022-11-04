package groupId.artifactId.service.api;

public interface IServiceUpdate<TYPE, TYPE2> {
    TYPE update(TYPE2 type, String id, String version);
}
