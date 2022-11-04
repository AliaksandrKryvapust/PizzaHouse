package groupId.artifactId.exceptions;

public class IncorrectOrderInputException extends RuntimeException {
    public IncorrectOrderInputException(String errorMsg) {
        super(errorMsg);
    }
}
