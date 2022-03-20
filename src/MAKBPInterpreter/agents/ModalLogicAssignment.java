package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.PropositionalLogicAssignment;

/**
 * Represents an assignment for modal and epistemic logic and its extension.
 * 
 * We can note that this logic is an extension of the propositional logic.
 */
public class ModalLogicAssignment extends PropositionalLogicAssignment {
    /**
     * Kripke structure to evaluate the state.
     */
    protected KripkeStructure structure;

    /**
     * World of the Kripke Structure to evaluate the state.
     */
    protected KripkeWorld world;

    /**
     * Constructor.
     * 
     * @param state     the state to evaluate
     * @param structure structure where evaluate the state
     * @param world     world where evaluate the state inside the structure
     */
    public ModalLogicAssignment(Map<Atom, Boolean> state, KripkeStructure structure, KripkeWorld world) {
        super(state);
        this.structure = structure;
        this.world = world;
    }

    /**
     * Gets the Kripke structure.
     * 
     * @return Kripke structure where to evaluate the state
     */
    public KripkeStructure getStructure() {
        return this.structure;
    }

    /**
     * Gets the world.
     * 
     * @return world where to evaluate the state inside the Kripke structure
     */
    public KripkeWorld getWorld() {
        return this.world;
    }
}
