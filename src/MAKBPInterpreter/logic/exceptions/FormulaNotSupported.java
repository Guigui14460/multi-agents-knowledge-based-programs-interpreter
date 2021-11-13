package MAKBPInterpreter.logic.exceptions;

/**
 * Exception which indicate that the given formula is not supported yet.
 */
public class FormulaNotSupported extends Exception {
    /**
     * Constructs a new exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause(Throwable)}.
     * 
     * @param arg0 message the detail message. The detail message is saved for later
     *             retrieval by the {@link #getMessage()} method
     */
    public FormulaNotSupported(String arg0) {
        super(arg0);
    }
}
