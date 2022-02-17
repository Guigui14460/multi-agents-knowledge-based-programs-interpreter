package MAKBPInterpreter.agents.exceptions;

/**
 * Exception class called when no world in a
 * {@link MAKBPInterpreter.agents.KripkeStructure} instance is possible.
 */
public class NoKripkeWorldPossibleException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause(Throwable)}.
     * 
     * @param arg0 message the detail message. The detail message is saved for later
     *             retrieval by the {@link #getMessage()} method
     */
    public NoKripkeWorldPossibleException(String arg0) {
        super("No Kripke world is possible. No convergence available. Explanation: " + arg0);
    }

    /**
     * Constructs a new exception without the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause(Throwable)}.
     */
    public NoKripkeWorldPossibleException() {
        super("No Kripke world is possible. No convergence available.");
    }
}
