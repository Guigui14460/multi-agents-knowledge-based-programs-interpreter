package MAKBPInterpreter.logic;

import java.util.Map;

/**
 * Represents the equivalence of a formula to an other.
 */
public class Equivalence implements Formula {
    /**
     * Represents the condition.
     */
    private Formula leftOperand;

    /**
     * Represents the new statement.
     */
    private Formula rightOperand;

    /**
     * Constructor.
     * 
     * @param leftOperand  left side operand
     * @param rightOperand right side operand
     */
    public Equivalence(Formula leftOperand, Formula rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return "(" + this.leftOperand.toString() + ") <-> (" + this.rightOperand.toString() + ")";
    }

    /**
     * Checks if the formulas are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Equivalence))
            return false;

        Equivalence otherBiImply = (Equivalence) other;
        return this.leftOperand.equals(otherBiImply.leftOperand) && this.rightOperand.equals(otherBiImply.rightOperand);
    }

    @Override
    public Formula simplify() {
        return new And(new Implication(this.leftOperand, this.rightOperand).simplify(),
                new Implication(this.rightOperand, this.leftOperand).simplify()).simplify();
    }

    @Override
    public Formula getNegation() {
        return new Not(this.simplify()).simplify();
    }

    @Override
    public boolean contains(Formula otherFormula) {
        return this.leftOperand.contains(otherFormula) || this.rightOperand.contains(otherFormula);
    }

    /**
     * Gets the left operand of the formula.
     * 
     * @return condition formula
     */
    public Formula getLeftOperand() {
        return this.leftOperand;
    }

    /**
     * Gets the right operand of the formula.
     * 
     * @return new formula implied by the left one
     */
    public Formula getRightOperand() {
        return this.rightOperand;
    }

    @Override
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        boolean evalLeft = this.leftOperand.evaluate(state, objects);
        boolean evalRight = this.rightOperand.evaluate(state, objects);

        return (!evalLeft || evalRight) && (!evalRight || evalLeft);
    }
}
