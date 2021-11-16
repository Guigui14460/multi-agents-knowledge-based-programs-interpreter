package MAKBPInterpreter.agents;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents an agent knowledge about the world.
 */
public class AgentKnowledge implements Formula {
    /**
     * Inner formula.
     */
    private Formula innerFormula;

    /**
     * Associated agent.
     */
    private Agent agent;

    public AgentKnowledge(Agent agent, Formula formula) {
        this.agent = agent;
        this.innerFormula = formula;
    }

    @Override
    public String toString() {
        return "K_{" + agent.getName() + "}(" + this.innerFormula.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AgentKnowledge)) {
            return false;
        }
        AgentKnowledge otherAgentKnowledge = (AgentKnowledge) other;
        return otherAgentKnowledge.agent.equals(this.agent)
                && this.innerFormula.equals(otherAgentKnowledge.innerFormula);
    }

    @Override
    public Formula simplify() {
        return this;
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
     * Gets the agent.
     * 
     * @return agent
     * @see #agent
     */
    public Agent getAgent() {
        return this.agent;
    }
}
