package MAKBPInterpreter.agents;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;

/**
 * Represents an agent evolving into an environment.
 */
public class Agent {
    /**
     * Agent name.
     */
    private String name;

    /**
     * Program used to select the right associated action to an observation.
     * 
     * If a null formula is provided, the agent deduce that is the else case.
     */
    private AgentProgram program;

    /**
     * Last selected action linked to the last seen observation.
     */
    private Action lastSelectedAction = null;

    /**
     * Constructor.
     * 
     * @param name    agent name
     * @param program used to select the right associated action to an
     *                observation
     */
    public Agent(String name, AgentProgram program) {
        this.name = name;
        this.program = program;
    }

    /**
     * Gets the associated action of an observation.
     * 
     * @param observation observation of the environment
     * @return associated action
     */
    public Action getAssociatedAction(Observation observation) {
        if (this.program.containsKey(observation.getFormula())) {
            this.lastSelectedAction = this.program.get(observation.getFormula());
            return this.lastSelectedAction;
        }
        if (this.program.containsKey(null)) {
            this.lastSelectedAction = this.program.get(null);
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

        Set<Formula> formulasElse = new HashSet<>();
        Set<Formula> formulaIfElseIf = new HashSet<>();
        for (Map.Entry<Formula, Action> entry : this.program.entrySet()) {
            if (selectedAction.equals(entry.getValue())) {
                formulaIfElseIf.add(entry.getKey());
            } else {
                formulasElse.add(new Not(entry.getKey()));
            }
        }

        if (formulaIfElseIf.size() == 0 || (formulaIfElseIf.size() == 1 && formulaIfElseIf.contains(null))) {
            return new And(formulasElse).simplify();
        }
        // if the action selected is in multiple program value
        return new Or(formulaIfElseIf).simplify();
    }

    /**
     * Performs the selected action corresponding an observation.
     * 
     * @param observation observation of an environment
     * @param objects     any number or type of object arguments to pass to action
     *                    performer
     * @return action return
     * @throws Exception throws when receive illegal arguments, objects cannot be
     *                   processed, etc.
     *                   Thrown by the action when executing.
     * 
     * @see #getAssociatedAction(Observation)
     * @see Action#performs(Object...)
     */
    public Object performsAssociatedAction(Observation observation, Object... objects) throws Exception {
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
            return agent.name.equals(this.name) && agent.program.equals(this.program);
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

    /**
     * Gets the program for the agent.
     * 
     * @return agent program
     */
    public AgentProgram getProgram() {
        return this.program;
    }
}
