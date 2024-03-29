package kinasr.nsr_yaml.exception;

/**
 * Class InvalidKeyException
 * <p>
 * This class extends the RuntimeException class and represents an exception that is thrown when
 * an invalid key is used.
 */
public class InvalidKeyException extends RuntimeException {

    /**
     * Throw {@link InvalidKeyException} without any attachments
     */
    public InvalidKeyException() {
    }

    /**
     * Throw {@link InvalidKeyException} with custom message
     *
     * @param message exception message
     */
    public InvalidKeyException(String message) {
        super(message);
    }

    /**
     * Throw {@link InvalidKeyException} with custom message and the cause
     *
     * @param message exception message
     * @param cause   the cause of the exception
     */
    public InvalidKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Throw {@link InvalidKeyException} with the cause
     *
     * @param cause the cause of the exception
     */
    public InvalidKeyException(Throwable cause) {
        super(cause);
    }
}
