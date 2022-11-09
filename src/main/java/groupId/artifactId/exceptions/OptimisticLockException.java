package groupId.artifactId.exceptions;

public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String errorMsg) {
        super(errorMsg);
    }
}
