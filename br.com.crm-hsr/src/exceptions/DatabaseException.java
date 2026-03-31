package exceptions;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String s) {
        super(s);
    }
}
