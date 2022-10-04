package groupId.artifactId.exceptions;

public class IncorrectEncodingException extends RuntimeException{
    public IncorrectEncodingException(String errorMsg, Throwable error) {
        super(errorMsg, error);
    }
}
