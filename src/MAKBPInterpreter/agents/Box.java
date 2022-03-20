package MAKBPInterpreter.agents;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.LogicAssignment;

/**
 * Represents a knowledge that agent knows it true in a world in a modal logic
 * system.
 */
public class Box extends AgentKnowledge {
    /**
     * Constructor.
     * 
     * @param agent   agent who knows the {@code formula}
     * @param formula formula known by the {@code agent}
     */
    public Box(Agent agent, Formula formula) {
        super(agent, formula);
    }

    @Override
    public Formula simplify() {
        return new AgentKnowledge(this.agent, this.innerFormula.simplify());
    }

    @Override
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        if (!(assignment instanceof ModalLogicAssignment)) {
            throw new IllegalArgumentException("the assignment must be a modal logic assignment");
        }

        ModalLogicAssignment assignment2 = (ModalLogicAssignment) assignment;
        KripkeWorld world = assignment2.getWorld();
        KripkeStructure structure = assignment2.getStructure();

        // we check if all connected worlds to the actual world satisfied the formula or
        // not
        // in other words, if it satisfied, it's because the world is a correct one for
        // the agent
        //
        // (M, s) |= K_i(phi) iff for all t, (M,t) |= phi, (s,t) e K_i(s)
        boolean result = true;
        for (KripkeWorld otherWorld : structure.getWorldsFromOtherWorldAndAgent(world, agent)) {
            result = result && otherWorld.satisfied(this.innerFormula, structure);
        }
        return result;
    }
}
