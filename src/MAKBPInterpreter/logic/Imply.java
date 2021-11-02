package MAKBPInterpreter.logic;

/**
 * Represents the implication of a formula to an other.
 */
public class Imply implements Formula {
    /**
     * Represents the condition.
     */
    Formula left;

    /**
     * Represents the new statement.
     */
    Formula right;

    public Imply(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + ") -> (" + this.right.toString() + ")";
    }

    /**
     * Checks if the formulas are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Imply)) {
            return false;
        }
        Imply otherImply = (Imply) other;
        return this.left.equals(otherImply.left) && this.right.equals(otherImply.right);
    }

    /**
     * Gets the left formula of the formula.
     * 
     * @return condition formula
     */
    public Formula getLeftFormula() {
        return this.left;
    }

    /**
     * Gets the right formula of the formula.
     * 
     * @return new formula implied by the left one
     */
    public Formula getRightFormula() {
        return this.right;
    }

    @Override
    public Formula simplify() {
        return new Or(new Not(this.left), this.right).simplify();
    }

    @Override
    public Formula getNegation() {
        return this.simplify().getNegation();
    }
}
