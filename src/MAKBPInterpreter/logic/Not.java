package MAKBPInterpreter.logic;

/**
 * Represents the negation of a formula.
 */
public class Not implements Formula {
    /**
     * Inner formula.
     */
    private Formula innerFormula;

    /**
     * Default constructor.
     * 
     * @param innerFormula formula to make the negation
     */
    public Not(Formula innerFormula) {
        this.innerFormula = innerFormula;
    }

    @Override
    public String toString() {
        return "~(" + this.innerFormula.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (!other.getClass().equals(this.getClass())) {
            return false;
        }
        Not otherNot = (Not) other;
        return this.innerFormula.equals(otherNot.innerFormula);
    }

    @Override
    public Formula simplify() {
        if (this.innerFormula.getClass().equals(this.getClass())) {
            Not subformula = (Not) this.innerFormula;
            return subformula.innerFormula.simplify();
        }
        if (this.innerFormula instanceof And || this.innerFormula instanceof Or || this.innerFormula instanceof Imply
                || this.innerFormula instanceof BidirectionnalImply) {
            return this.innerFormula.getNegation().simplify();
        }
        return this;
    }

    /**
     * Gets the inner formula object.
     * 
     * @return inner formula object
     * @see #innerFormula
     */
    public Formula getInnerFormula() {
        return this.innerFormula;
    }

    @Override
    public Formula getNegation() {
        return new Not(this).simplify();
    }
}
