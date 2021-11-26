package MAKBPInterpreter.logic;

import java.util.Map;

/**
 * Represents the implication of a formula to an other.
 */
public class Implication implements Formula {
    /**
     * Represents the condition.
     */
    Formula leftOperand;

    /**
     * Represents the new statement.
     */
    Formula rightOperand;

    /**
     * Constructor.
     * 
     * @param leftOperand  left side operand
     * @param rightOperand right side operand
     */
    public Implication(Formula leftOperand, Formula rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return "(" + this.leftOperand.toString() + ") -> (" + this.rightOperand.toString() + ")";
    }

    /**
     * Checks if the formulas are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Implication))
            return false;

        Implication otherImply = (Implication) other;
        return this.leftOperand.equals(otherImply.leftOperand) && this.rightOperand.equals(otherImply.rightOperand);
    }

    @Override
    public Formula simplify() {
        return new Or(new Not(this.leftOperand).simplify(), this.rightOperand.simplify()).simplify();
    }

    @Override
    public Formula getNegation() {
        return this.simplify().getNegation();
    }

    @Override
    public boolean contains(Formula otherFormula) {
        return this.leftOperand.contains(otherFormula) || this.rightOperand.contains(otherFormula);
    }

    /**
     * Gets the left operand of the formula.
     * 
     * @return condition operand
     */
    public Formula getLeftOperand() {
        return this.leftOperand;
    }

    /**
     * Gets the right operand of the formula.
     * 
     * @return new operand implied by the left one
     */
    public Formula getRightOperand() {
        return this.rightOperand;
    }

    @Override
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        return !this.leftOperand.evaluate(state, objects) || this.rightOperand.evaluate(state, objects);
    }
}
