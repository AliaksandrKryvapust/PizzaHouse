package groupId.artifactId.exceptions;

public class DaoException extends RuntimeException {
    public DaoException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
