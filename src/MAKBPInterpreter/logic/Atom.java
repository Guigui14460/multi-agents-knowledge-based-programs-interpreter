package MAKBPInterpreter.logic;

/**
 * Represents an atom, a proposition.
 */
public class Atom implements Formula {
    /**
     * Predicate of the proposition.
     */
    private String predicate;

    /**
     * Default constructor of the atom.
     * 
     * @param predicate proposition predicate
     */
    public Atom(String predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return this.predicate;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Atom))
            return false;

        Atom otherAtom = (Atom) other;
        return predicate.equals(otherAtom.predicate);
    }

    @Override
    public Formula simplify() {
        return this;
    }

    @Override
    public Formula getNegation() {
        return new Not(this).simplify();
    }

    @Override
    public boolean contains(Formula otherFormula) {
        return this.equals(otherFormula);
    }
}
