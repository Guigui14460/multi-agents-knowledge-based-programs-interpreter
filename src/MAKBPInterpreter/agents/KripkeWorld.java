package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.exceptions.FormulaNotSupported;

/**
 * Class representing a world in a
 * {@link MAKBPInterpreter.agents.KripkeStructure} (node of the graph in this
 * structure).
 */
public class KripkeWorld {
    /**
     * Used if no world name is given.
     */
    private static Long id = 0L;

    /**
     * Name of the world.
     */
    private String name;

    /**
     * True propositions.
     */
    private Map<Atom, Boolean> assignment;

    /**
     * Default constructor.
     * 
     * @param name       world name
     * @param assignment true proposition of the world
     */
    public KripkeWorld(String name, Map<Atom, Boolean> assignment) {
        this.name = name;
        this.assignment = assignment;
        KripkeWorld.id++;
    }

    /**
     * Constructor without world name.
     * 
     * @param assignment true proposition of the world
     */
    public KripkeWorld(Map<Atom, Boolean> assignment) {
        this(KripkeWorld.id.toString(), assignment);
    }

    public boolean satisfied(Formula formula) throws FormulaNotSupported {
        return formula.evaluate(this.assignment);
    }

    @Override
    public String toString() {
        return "World[" + this.name + "," + this.assignment.toString() + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof KripkeWorld) {
            KripkeWorld world = (KripkeWorld) other;
            return world.name.equals(this.name) && world.assignment.equals(this.assignment);
        }
        return false;
    }

    /**
     * Gets the name of the world.
     * 
     * @return world name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets all the true propositions of the world.
     * 
     * @return true propositions
     */
    public Map<Atom, Boolean> getAssignment() {
        return this.assignment;
    }
}
