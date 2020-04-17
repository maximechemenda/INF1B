/**
 * Different available commonly used exception messages.
 */
public enum ExceptionMessage {
    NULL_DATA("Given data must not be null."),
    NULL_ARGUMENT("Given argument input must not be null."),
    NULL_BOOKS("Given books must not be null."),
    NULL_BOOK_ENTRIES("Given books entries must not be null.");

    /**
     * Exception message.
     */
    private String exceptionMessage;

    /**
     * Create an exception message.
     *
     * @param exceptionMessage Exception message.
     */
    private ExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Get the exception message.
     *
     * @return Exception message.
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
