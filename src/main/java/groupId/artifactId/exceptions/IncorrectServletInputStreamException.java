package groupId.artifactId.exceptions;

public class IncorrectServletInputStreamException extends RuntimeException{
    public IncorrectServletInputStreamException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
