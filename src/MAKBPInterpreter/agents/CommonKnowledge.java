package MAKBPInterpreter.agents;

import java.util.Set;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a set of agents knowing a common formula.
 */
public class CommonKnowledge implements Formula {
    /**
     * Agent set where each agent knows the associated formula.
     */
    private Set<Agent> agents;

    /**
     * Common formula knows by all the agents.
     */
    private Formula innerFormula;

    /**
     * Default constructor.
     * 
     * @param agents  set of agents where each of this knows the {@code formula}
     * @param formula knowledge
     */
    public CommonKnowledge(Set<Agent> agents, Formula formula) {
        this.agents = agents;
        this.innerFormula = formula;
    }

    @Override
    public Formula simplify() {
        return new CommonKnowledge(this.agents, this.innerFormula.simplify());
    }

    @Override
    public Formula getNegation() {
        return new Not(this);
    }

    @Override
    public boolean contains(Formula otherFormula) {
        return this.innerFormula.contains(otherFormula);
    }

    /**
     * Gets the inner formula object.
     * 
     * @return inner formula object
     * @see #innerFormula
     */
    public Formula getInnerFormula() {
        return this.innerFormula;
    }

    /**
     * Gets the agent set where each agent knows the associated formula.
     * 
     * @return agent set
     */
    public Set<Agent> getAgents() {
        return this.agents;
    }
}
