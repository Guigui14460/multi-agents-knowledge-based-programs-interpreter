package MAKBPInterpreter.agents;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;

// TODO: maj des docs pour les conditions
/**
 * Represents an agent evolving into an environment.
 */
public class Agent {
    /**
     * Agent name.
     */
    private String name;

    /**
     * Conditions used to select the right associated action to an observation.
     * 
     * If a null formula is provided, the agent deduce that is the else case.
     */
    private SortedMap<Formula, Action> conditions;

    /**
     * Last selected action linked to the last seen observation.
     */
    private Action lastSelectedAction = null;

    /**
     * Constructor.
     * 
     * @param name       agent name
     * @param conditions used to select the right associated action to an
     *                   observation
     */
    public Agent(String name, SortedMap<Formula, Action> conditions) {
        this.name = name;
        this.conditions = conditions;
    }

    /**
     * Gets the associated action of an observation.
     * 
     * @param observation observation of the environment
     * @return associated action
     */
    public Action getAssociatedAction(Observation observation) {
        if (this.conditions.containsKey(observation.getFormula())) {
            this.lastSelectedAction = this.conditions.get(observation.getFormula());
            return this.lastSelectedAction;
        }
        if (this.conditions.containsKey(null)) {
            this.lastSelectedAction = this.conditions.get(null);
            return this.lastSelectedAction;
        }
        return null;
    }

    /**
     * Returns the associated formula to the passed action.
     * 
     * @param selectedAction the selected action to get the right initial formula of
     *                       the observation
     * @return associated formula or null object if no match
     */
    public Formula reverseEngineering(Action selectedAction) {
        if (selectedAction == null) {
            return null;
        }

        Set<Formula> formulas = new HashSet<>();
        for (Map.Entry<Formula, Action> entry : this.conditions.entrySet()) {
            if (selectedAction.equals(entry.getValue())) {
                formulas.add(entry.getKey());
                break;
            } else {
                formulas.add(new Not(entry.getKey()));
            }
        }
        return new Or(formulas).simplify();
    }

    /**
     * Performs the selected action corresponding an observation.
     * 
     * @param observation observation of an environment
     * @param objects     any number or type of object arguments to pass to action
     *                    performer
     * @return action return
     * 
     * @see #getAssociatedAction(Observation)
     * @see Action#performs(Object...)
     */
    public Object performsAssociatedAction(Observation observation, Object... objects) {
        return this.getAssociatedAction(observation).performs(objects);
    }

    /**
     * Returns the associated formula to the last selected action. Can be used in
     * any case.
     * 
     * @return associated formula or null object if no match
     * 
     * @see #lastSelectedAction
     * @see #reverseEngineering(Action)
     */
    public Formula reverseEngineering() {
        return this.reverseEngineering(this.lastSelectedAction);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Agent) {
            Agent agent = (Agent) other;
            return agent.name.equals(this.name);
        }
        return false;
    }

    /**
     * Gets the name of the agent.
     * 
     * @return agent name
     */
    public String getName() {
        return this.name;
    }
}
