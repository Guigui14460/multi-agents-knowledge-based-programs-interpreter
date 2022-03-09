package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a set of agents knowing a common formula.
 * 
 * This knowledge corresponding of EK^depth(EK^(depth-1) (... (EK(phi)))).
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
     * Depth of the knowledge (finite).
     */
    private int depth;

    /**
     * Default constructor.
     * 
     * @param formula knowledge
     * @param agents  set of agents where each of this knows the {@code formula}
     * @param depth   finite depth of knowledge of {@code formula} for
     *                {@code agents}
     */
    public CommonKnowledge(Formula formula, Set<Agent> agents, int depth) {
        this.agents = agents;
        this.innerFormula = formula;
        this.depth = depth;
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
                && this.innerFormula.equals(otherCommonKnowledge.innerFormula)
                && this.depth == otherCommonKnowledge.depth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agents, this.innerFormula, this.depth);
    }

    @Override
    public Formula simplify() {
        return new CommonKnowledge(this.innerFormula.simplify(), this.agents, this.depth);
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
        // (M, s) |= CK_J(phi) iff AND forall 0 < i <= depth, EK_J^{i}(EK_J^{i-1}(phi))
        // EK_J^0 (phi) = phi
        boolean result = this.innerFormula.evaluate(state, objects);
        Formula currentFormula = this.innerFormula;
        for (int i = 1; i <= depth; i++) {
            currentFormula = new EverybodyKnowledge(currentFormula, agents);
            result = result && currentFormula.evaluate(state, objects);
        }
        return result;
    }
}
