package MAKBPInterpreter.agents;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Or;

/**
 * Represents an agent evolving into an environment.
 */
public class Agent {
    /**
     * Agent name.
     */
    protected String name;

    /**
     * Program used to select the right associated action to an observation.
     * 
     * If a null formula is provided, the agent deduce that is the else case.
     */
    protected AgentProgram program;

    /**
     * Last selected action linked to the last seen observation.
     */
    protected Action lastSelectedAction = null;

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
     * Gets the associated action of a structure and a pointed world.
     * 
     * @param structure    Kripke structure
     * @param pointedWorld world to retrieve knowledge
     * 
     * @return associated action or null if no matched conditions and else is not
     *         present
     * @throws Exception throws when the formula not supported evaluate operation or
     *                   expected object not given
     */
    public Action getAssociatedAction(KripkeStructure structure, KripkeWorld pointedWorld) throws Exception {
        int i = -1;
        boolean condition_validate = false;
        ModalLogicAssignment assignment;
        while (!condition_validate) {
            i++;
            if (i >= this.program.size()) { // if no else is present
                return null;
            }

            Formula key = this.program.getKey(i);
            if (key != null) {
                assignment = new ModalLogicAssignment(pointedWorld.getAssignment(), structure, pointedWorld);
                condition_validate = key.evaluate(assignment);
            } else { // handle the else statement
                condition_validate = true;
            }
        }

        this.lastSelectedAction = this.program.getValue(i);
        return this.lastSelectedAction;
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
        Set<Formula> negativeConditions = new HashSet<>();

        for (int i = 0; i < this.program.size(); i++) {
            Formula formula = this.program.getKey(i);
            Action action = this.program.getValue(i);

            if (selectedAction.equals(action)) {
                Formula f = negativeConditions.size() == 0 ? null : new And(new HashSet<>(negativeConditions));
                formulas.add(new And(formula, f));
            }
            if (formula != null) {
                negativeConditions.add(formula.getNegation());
            }
        }

        return new Or(formulas).simplify();
    }

    /**
     * Performs the selected action corresponding a structure and a pointed world.
     * 
     * @param structure    Kripke structure
     * @param pointedWorld world to retrieve knowledge
     * @param objects      any number or type of object arguments to pass to action
     *                     performer
     * @return action return
     * @throws Exception throws when receive illegal arguments, objects cannot be
     *                   processed, etc or when the formula not supported this
     *                   operation or
     *                   expected object not given
     * 
     * @see #getAssociatedAction(KripkeStructure, KripkeWorld)
     * @see Action#performs(Object...)
     */
    public Object performsAssociatedAction(KripkeStructure structure, KripkeWorld pointedWorld, Object... objects)
            throws Exception {
        return this.getAssociatedAction(structure, pointedWorld).performs(objects);
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
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof Agent))
            return false;

        Agent agent = (Agent) other;
        return agent.name.equals(this.name) && agent.program.equals(this.program);
    }

    @Override
    public String toString() {
        return "Agent " + this.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.program);
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
