package MAKBPInterpreter.agents;

/**
 * Represents an action for an agent.
 */
public interface Action {
    /**
     * Performs custom action.
     * 
     * @param objects any number or type of object arguments
     * @return result object
     */
    public Object performs(Object... objects);
}
