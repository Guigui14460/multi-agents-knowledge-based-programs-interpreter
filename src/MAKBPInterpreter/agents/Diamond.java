package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;

public class Diamond extends AgentKnowledge {
    public Diamond(Agent agent, Formula formula) {
        super(agent, formula);
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
        for (KripkeWorld otherWorld : structure.getWorldFromOtherWorldAndAgent(world, agent)) {
            result = result || otherWorld.satisfied(this.innerFormula, structure);
        }
        return result;
    }
}
