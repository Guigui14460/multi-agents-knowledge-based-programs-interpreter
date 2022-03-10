package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
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
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        // we have always two additionnal arguments but we verify that is the case
        if (objects.length < 2) {
            throw new IllegalArgumentException("We must have at least the world and the Kripke structure");
        }
        KripkeWorld world = (KripkeWorld) objects[0];
        KripkeStructure structure = (KripkeStructure) objects[1];

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
