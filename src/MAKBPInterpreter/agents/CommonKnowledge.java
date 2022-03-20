package MAKBPInterpreter.agents;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.LogicAssignment;
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

    /**
     * Evaluates the everybody knowledge of {@link CommonKnowledge#innerFormula
     * formula} in all accessible worlds by all
     * agents of the group {@link CommonKnowledge#agents}.
     * 
     * An adapted Breadth-first search where at each world, an evaluation of EK was
     * realized.
     * This algorithm represents the infinite depth in theory but limited by the
     * number of edges and vertices of the Kripke structure.
     */
    @Override
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        if (!(assignment instanceof ModalLogicAssignment)) {
            throw new IllegalArgumentException("the assignment must be a modal logic assignment");
        }

        ModalLogicAssignment assignment2 = (ModalLogicAssignment) assignment;
        KripkeWorld pointedWorld = assignment2.getWorld();
        KripkeStructure structure = assignment2.getStructure();

        Formula formula = new EverybodyKnowledge(this.innerFormula, agents);

        Queue<KripkeWorld> queue = new LinkedList<>();
        Set<KripkeWorld> coloredWorlds = new HashSet<>();
        queue.add(pointedWorld);
        coloredWorlds.add(pointedWorld);
        while (!queue.isEmpty()) {
            KripkeWorld world = queue.poll();

            if (!world.satisfied(formula, structure)) {
                return false;
            }

            for (Agent agent : this.agents) {
                for (KripkeWorld t : structure.getWorldsFromOtherWorldAndAgent(world, agent)) {
                    if (!coloredWorlds.contains(t)) {
                        queue.add(t);
                        coloredWorlds.add(t);
                    }
                }
            }
        }

        return true;
    }
}
