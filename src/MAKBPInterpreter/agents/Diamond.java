package MAKBPInterpreter.agents;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.LogicAssignment;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a knowledge that an agent considers to be true between world in a
 * modal logic system.
 */
public class Diamond extends AgentKnowledge {
    /**
     * Constructor.
     * 
     * @param agent   agent who considers the {@code formula} to be true
     * @param formula considered formula by the {@code agent}
     */
    public Diamond(Agent agent, Formula formula) {
        super(agent, formula);
    }

    @Override
    public Formula simplify() {
        return new Not(new AgentKnowledge(this.agent, new Not(this.innerFormula)));
    }

    @Override
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        if (!(assignment instanceof ModalLogicAssignment)) {
            throw new IllegalArgumentException("the assignment must be a modal logic assignment");
        }

        ModalLogicAssignment assignment2 = (ModalLogicAssignment) assignment;
        KripkeWorld world = assignment2.getWorld();
        KripkeStructure structure = assignment2.getStructure();

        // we check if any connected worlds to the actual world satisfied the formula or
        // not
        // in other words, if it satisfied, it's because the world is a correct one for
        // the agent
        //
        // (M, s) |= K_i(phi) iff for any t, (M,t) |= phi, (s,t) e K_i(s)
        boolean result = false;
        for (KripkeWorld otherWorld : structure.getWorldsFromOtherWorldAndAgent(world, agent)) {
            result = result || otherWorld.satisfied(this.innerFormula, structure);
        }
        return result;
    }
}
