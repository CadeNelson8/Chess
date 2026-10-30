package dataaccess;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Exception ex) {
        super(message, ex);
    }
}
