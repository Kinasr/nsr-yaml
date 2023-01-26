package exception;

public class YAMLFileException extends RuntimeException{
    public YAMLFileException() {
        super();
    }

    public YAMLFileException(String message) {
        super(message);
    }

    public YAMLFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAMLFileException(Throwable cause) {
        super(cause);
    }
}
