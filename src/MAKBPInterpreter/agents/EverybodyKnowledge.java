package MAKBPInterpreter.agents;

import java.util.Objects;
import java.util.Set;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.LogicAssignment;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a set of agents knowing a common formula.
 */
public class EverybodyKnowledge implements Formula {
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
    public EverybodyKnowledge(Formula formula, Set<Agent> agents) {
        this.agents = agents;
        this.innerFormula = formula;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof EverybodyKnowledge))
            return false;

        EverybodyKnowledge otherEverybodyKnowledge = (EverybodyKnowledge) other;
        return otherEverybodyKnowledge.agents.equals(this.agents)
                && this.innerFormula.equals(otherEverybodyKnowledge.innerFormula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agents, this.innerFormula);
    }

    @Override
    public Formula simplify() {
        return new EverybodyKnowledge(this.innerFormula.simplify(), this.agents);
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
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        if (!(assignment instanceof ModalLogicAssignment)) {
            throw new IllegalArgumentException("the assignment must be a modal logic assignment");
        }

        ModalLogicAssignment assignment2 = (ModalLogicAssignment) assignment;
        KripkeWorld world = assignment2.getWorld();
        KripkeStructure structure = assignment2.getStructure();

        boolean result = true;
        // (M, s) |= EK_J(phi) iff forall t, (M,t) |= phi, (s,t) e (forall i e J,
        // K_i(s))
        for (Agent agent : this.agents) {
            for (KripkeWorld otherWorld : structure.getWorldsFromOtherWorldAndAgent(world, agent)) {
                result = result && otherWorld.satisfied(this.innerFormula, structure);
            }
        }
        return result;
    }
}
