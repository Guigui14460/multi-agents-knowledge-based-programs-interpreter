package MAKBPInterpreter.logic;

import java.util.Objects;

/**
 * Represents the negation of a formula.
 */
public class Not implements Formula {
    /**
     * Operand of the logic operation.
     */
    private Formula operand;

    /**
     * Default constructor.
     * 
     * @param operand formula to make the negation
     */
    public Not(Formula operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        return "~(" + this.operand.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof Not))
            return false;

        Not otherNot = (Not) other;
        return this.operand.equals(otherNot.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.operand);
    }

    @Override
    public Formula simplify() {
        if (this.operand == null) {
            return null;
        }

        if (this.operand.getClass().equals(this.getClass())) {
            Not subformula = (Not) this.operand;
            return subformula.operand.simplify();
        }
        if (this.operand instanceof And || this.operand instanceof Or || this.operand instanceof Implication
                || this.operand instanceof Equivalence) {
            return this.operand.getNegation().simplify();
        }
        return this;
    }

    @Override
    public Formula getNegation() {
        return new Not(this).simplify();
    }

    @Override
    public boolean contains(Formula otherFormula) {
        return this.equals(otherFormula) || this.operand.contains(otherFormula);
    }

    /**
     * Gets the operand object.
     * 
     * @return operand object
     * @see #operand
     */
    public Formula getOperand() {
        return this.operand;
    }

    @Override
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        return !this.operand.evaluate(assignment);
    }
}
