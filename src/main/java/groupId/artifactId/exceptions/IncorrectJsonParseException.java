package groupId.artifactId.exceptions;

public class IncorrectJsonParseException extends RuntimeException{
    public IncorrectJsonParseException (String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
