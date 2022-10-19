package groupId.artifactId.exceptions;

public class IncorrectDataSourceException extends RuntimeException {
    public IncorrectDataSourceException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
