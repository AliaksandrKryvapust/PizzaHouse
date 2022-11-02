package groupId.artifactId.controller.validator.api;

public interface IValidator<TYPE> {
    void validate(TYPE type);
}
