package groupId.artifactId.exceptions;

public class IncorrectServletRedirectException extends RuntimeException{
    public IncorrectServletRedirectException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
