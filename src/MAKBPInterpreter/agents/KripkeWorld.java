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
     * Propositions of the world represents.
     */
    private Map<Atom, Boolean> assignment;

    /**
     * Default constructor.
     * 
     * @param name       world name
     * @param assignment proposition of the world represents
     */
    public KripkeWorld(String name, Map<Atom, Boolean> assignment) {
        this.name = name;
        this.assignment = assignment;
        KripkeWorld.id++;
    }

    /**
     * Constructor without world name.
     * 
     * @param assignment proposition of the world represents
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
        return formula.evaluate(new ModalLogicAssignment(this.assignment, structure, this));
    }

    /**
     * Returns the differences between two worlds (all atoms divergence).
     * 
     * @param world other world
     * @return atoms set representing the divergence
     */
    public Collection<Atom> differencesBetweenWorlds(KripkeWorld world) {
        Set<Atom> union = new HashSet<>();
        union.addAll(this.assignment.keySet());
        union.addAll(world.assignment.keySet());

        Set<Atom> intersection = new HashSet<>();
        intersection.addAll(this.assignment.keySet());
        intersection.retainAll(world.assignment.keySet());

        union.removeAll(intersection);

        // check if atom in intersection have different values
        for (Atom atom : intersection) {
            if (world.assignment.get(atom) != this.assignment.get(atom)) {
                union.add(atom);
            }
        }

        return union;
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
