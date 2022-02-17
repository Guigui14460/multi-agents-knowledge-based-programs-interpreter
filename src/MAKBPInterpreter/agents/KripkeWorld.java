package MAKBPInterpreter.agents;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;

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

    /**
     * Checks if a given formula is satisfying the actual state of the world.
     * 
     * @param formula   formula to satisfied
     * @param structure Kripke structure
     * @return boolean representing if the world satisfied the formula
     * @throws Exception thrown if an illegal argument is passed
     */
    public boolean satisfied(Formula formula, KripkeStructure structure) throws Exception {
        System.out.println("--------- World Evaluation : " + this);
        return formula.evaluate(this.assignment, this, structure);
    }

    /**
     * Returns the differences between two worlds (all atoms divergence).
     * 
     * @param world other world
     * @return atoms collection representing the divergence
     */
    public Collection<Atom> differencesBetweenWorlds(KripkeWorld world) {
        Set<Atom> atoms = new HashSet<>();
        for (Map.Entry<Atom, Boolean> entry : this.assignment.entrySet()) {
            if (world.assignment.get(entry.getKey()) != entry.getValue()) {
                atoms.add(entry.getKey());
            }
        }
        return atoms;
    }

    @Override
    public String toString() {
        return "World[" + this.name + "," + this.assignment.toString() + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof KripkeWorld))
            return false;

        KripkeWorld world = (KripkeWorld) other;
        if (!world.name.equals(this.name)) {
            return false;
        }

        if (!world.assignment.equals(this.assignment)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.assignment);
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
