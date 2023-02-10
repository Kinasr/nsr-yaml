package exception;

/**
 * YAMLFileException Class
 * <p>
 * The YAMLFileException class extends the RuntimeException class and represents an error that occurred while
 * reading or writing a YAML file.
 */
public class YAMLFileException extends RuntimeException {

    /**
     * Throw {@link YAMLFileException} without any attachments
     */
    public YAMLFileException() {
        super();
    }

    /**
     * Throw {@link YAMLFileException} with custom message
     *
     * @param message exception message
     */
    public YAMLFileException(String message) {
        super(message);
    }

    /**
     * Throw {@link YAMLFileException} with custom message and the cause
     *
     * @param message exception message
     * @param cause   the cause of the exception
     */
    public YAMLFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Throw {@link YAMLFileException} with the cause
     *
     * @param cause the cause of the exception
     */
    public YAMLFileException(Throwable cause) {
        super(cause);
    }
}
