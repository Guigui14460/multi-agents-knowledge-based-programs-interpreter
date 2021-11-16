package MAKBPInterpreter.agents.exceptions;

/**
 * Exception class called when an invalid thing was arrived in the computation
 * in a {@link MAKBPInterpreter.agents.KripkeStructure} instance is possible.
 */
public class KripkeStructureInvalidRuntimeException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause(Throwable)}.
     * 
     * @param arg0 message the detail message. The detail message is saved for later
     *             retrieval by the {@link #getMessage()} method
     */
    public KripkeStructureInvalidRuntimeException(String arg0) {
        super(arg0);
    }
}
