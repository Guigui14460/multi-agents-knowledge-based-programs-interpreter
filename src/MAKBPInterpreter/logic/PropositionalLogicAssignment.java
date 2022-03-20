package MAKBPInterpreter.logic;

import java.util.Map;

/**
 * Represents an assignment for propositional logic and its extensions
 */
public class PropositionalLogicAssignment implements LogicAssignment {
    /**
     * State to evaluate.
     */
    protected Map<Atom, Boolean> state;

    /**
     * Constructor.
     * 
     * @param state the state to evaluate
     */
    public PropositionalLogicAssignment(Map<Atom, Boolean> state) {
        this.state = state;
    }

    /**
     * Gets the associated value of a key.
     * 
     * @param key key to gets the value
     * @return the associated value
     */
    public boolean get(Atom key) {
        return this.state.get(key);
    }

    /**
     * Gets the associated value of a key if exists in the state, else the default
     * value.
     * 
     * @param key          key to gets the value
     * @param defaultValue default value to return if the key doesn't exists in the
     *                     state
     * @return the associated value or default value
     */
    public boolean getOrDefault(Atom key, boolean defaultValue) {
        return this.state.getOrDefault(key, defaultValue);
    }
}
