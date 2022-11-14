package groupId.artifactId.exceptions;

public class ServiceException extends RuntimeException {
    public ServiceException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
