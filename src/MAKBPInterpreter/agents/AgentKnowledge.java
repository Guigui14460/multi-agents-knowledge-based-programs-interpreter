package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Objects;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents an agent knowledge about the world.
 */
public class AgentKnowledge implements Formula {
    /**
     * Inner formula.
     */
    protected Formula innerFormula;

    /**
     * Associated agent.
     */
    protected Agent agent;

    /**
     * Constructor.
     * 
     * @param agent   agent who knows the {@code formula}
     * @param formula formula known by the {@code agent}
     */
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
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof AgentKnowledge))
            return false;

        AgentKnowledge otherAgentKnowledge = (AgentKnowledge) other;
        return otherAgentKnowledge.agent.equals(this.agent)
                && this.innerFormula.equals(otherAgentKnowledge.innerFormula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agent.getName(), this.innerFormula);
    }

    @Override
    public Formula simplify() {
        return new AgentKnowledge(this.agent, this.innerFormula.simplify());
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
     * Gets the agent.
     * 
     * @return agent
     * @see #agent
     */
    public Agent getAgent() {
        return this.agent;
    }

    @Override
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        // we have always two additionnal arguments but we verify that is the case
        if (objects.length < 2) {
            throw new IllegalArgumentException("We must have at least the world and the Kripke structure");
        }
        KripkeWorld world = (KripkeWorld) objects[0];
        KripkeStructure structure = (KripkeStructure) objects[1];

        // we check if all connected worlds to the actual world satisfied the formula or
        // not
        // in other words, if it satisfied, it's because the world is a correct one for
        // the agent
        //
        // (M, s) |= K_i(phi) iff for all t, (M,t) |= phi, (s,t) e K_i(s)
        boolean result = true;
        System.out.println(this.innerFormula + " evaluation :");
        for (KripkeWorld otherWorld : structure.getWorldFromOtherWorldAndAgent(world, agent)) {
            System.out.println("Ka evaluate World nammed " + otherWorld.getName());
            boolean res = this.innerFormula.evaluate(otherWorld.getAssignment(), otherWorld, structure);
            System.out.println("==> " + res);
            result = result && res;
        }
        return result;
    }
}
