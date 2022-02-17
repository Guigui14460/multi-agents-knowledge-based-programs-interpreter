package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a set of agents knowing a common formula.
 */
public class CommonKnowledge implements Formula {
    /**
     * Agent set where each agent knows the associated formula.
     */
    protected Set<Agent> agents;

    /**
     * Common formula knows by all the agents.
     */
    protected Formula innerFormula;

    /**
     * Default constructor.
     * 
     * @param formula knowledge
     * @param agents  set of agents where each of this knows the {@code formula}
     */
    public CommonKnowledge(Formula formula, Set<Agent> agents) {
        this.agents = agents;
        this.innerFormula = formula;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof CommonKnowledge))
            return false;

        CommonKnowledge otherCommonKnowledge = (CommonKnowledge) other;
        return otherCommonKnowledge.agents.equals(this.agents)
                && this.innerFormula.equals(otherCommonKnowledge.innerFormula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agents, this.innerFormula);
    }

    @Override
    public Formula simplify() {
        return new CommonKnowledge(this.innerFormula.simplify(), this.agents);
    }

    @Override
    public Formula getNegation() {
        return new Not(this).simplify();
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

    @Override
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        // we have always two additionnal arguments but we verify that is the case
        if (objects.length < 2) {
            throw new IllegalArgumentException("We must have at least the world and the Kripke structure");
        }
        KripkeWorld world = (KripkeWorld) objects[0];
        KripkeStructure structure = (KripkeStructure) objects[1];
        boolean result = true;
        // (M, s) |= CK_J(phi) iff forall t, (M,t) |= phi, (s,t) e (forall i e J,
        // K_i(s))
        for (Agent agent : this.agents) {
            for (KripkeWorld otherWorld : structure.getWorldFromOtherWorldAndAgent(world, agent)) {
                result = result && otherWorld.satisfied(this.innerFormula, structure);
            }
        }
        return result;
    }
}
