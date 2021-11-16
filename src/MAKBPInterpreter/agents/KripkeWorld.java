package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Equivalence;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Implication;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
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

    public boolean satisfied(Formula formula, KripkeStructure structure) throws FormulaNotSupported {
        boolean ok;
        if (formula instanceof Atom) {
            Atom atom = (Atom) formula;
            ok = this.assignment.getOrDefault(atom, false);
        } else if (formula instanceof Not) {
            Not not = (Not) formula;
            ok = !this.satisfied(not.getOperand(), structure);
        } else if (formula instanceof Or) {
            ok = false;
            for (Formula innerOr : ((Or) formula).getOperands()) {
                ok |= this.satisfied(innerOr, structure);
            }
        } else if (formula instanceof And) {
            ok = true;
            for (Formula innerAnd : ((And) formula).getOperands()) {
                ok &= this.satisfied(innerAnd, structure);
            }
        } else if (formula instanceof Implication) {
            Implication implication = (Implication) formula;
            ok = !this.satisfied(implication.getLeftOperand(), structure)
                    || this.satisfied(implication.getRightOperand(), structure);
        } else if (formula instanceof Equivalence) {
            ok = this.satisfied(formula.simplify(), structure);
        } else if (formula instanceof AgentKnowledge) {
            AgentKnowledge k_a = (AgentKnowledge) formula;
            ok = true;
            for (KripkeWorld world : structure.getWorldFromOtherWorldAndAgent(this, k_a.getAgent())) {
                ok &= world.satisfied(k_a.getInnerFormula(), structure);
            }
        } else {
            throw new FormulaNotSupported("You need to derive and herite this method to handle your formula");
        }
        return ok;
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
