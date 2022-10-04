package groupId.artifactId.exceptions;

public class IncorrectServletWriterException extends RuntimeException {
    public IncorrectServletWriterException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
