package MAKBPInterpreter.logic;

/**
 * Represents the equivalence of a formula to an other.
 */
public class BidirectionnalImply implements Formula {
    /**
     * Represents the condition.
     */
    Formula left;

    /**
     * Represents the new statement.
     */
    Formula right;

    public BidirectionnalImply(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + ") <-> (" + this.right.toString() + ")";
    }

    /**
     * Checks if the formulas are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BidirectionnalImply)) {
            return false;
        }
        BidirectionnalImply otherBiImply = (BidirectionnalImply) other;
        return this.left.equals(otherBiImply.left) && this.right.equals(otherBiImply.right);
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
        return new And(new Imply(this.left, this.right).simplify(), new Imply(this.right, this.left).simplify())
                .simplify();
    }

    @Override
    public Formula getNegation() {
        return new Not(this.simplify()).simplify();
    }
}
