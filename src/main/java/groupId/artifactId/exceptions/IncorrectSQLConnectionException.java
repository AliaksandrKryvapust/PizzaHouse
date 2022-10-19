package groupId.artifactId.exceptions;

public class IncorrectSQLConnectionException extends RuntimeException{
    public IncorrectSQLConnectionException (String errorMsg, Throwable error){
        super(errorMsg,error);
    }
}
