package exception;

/**
 * <h1>ParsingException Class</h1>
 * <p>The ParsingException class extends the RuntimeException class and represents a parsing error that occurred
 * during the execution of a program.</p>
 */
public class ParsingException extends RuntimeException {

    /**
     * Throw {@link ParsingException} without any attachments
     */
    public ParsingException() {
    }

    /**
     * Throw {@link ParsingException} with custom message
     *
     * @param message exception message
     */
    public ParsingException(String message) {
        super(message);
    }

    /**
     * Throw {@link ParsingException} with custom message and the cause
     *
     * @param message exception message
     * @param cause   the cause of the exception
     */
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Throw {@link ParsingException} with the cause
     *
     * @param cause the cause of the exception
     */
    public ParsingException(Throwable cause) {
        super(cause);
    }
}
