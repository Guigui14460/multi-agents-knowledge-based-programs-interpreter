package MAKBPInterpreter.logic;

/**
 * Represents an atom, a proposition.
 */
public class Atom implements Formula {
    /**
     * Name of the proposition.
     */
    private String name;

    /**
     * Default constructor of the atom.
     * 
     * @param name proposition name
     */
    public Atom(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (!other.getClass().equals(this.getClass())) {
            return false;
        }
        Atom otherAtom = (Atom) other;
        return name.equals(otherAtom.name);
    }

    @Override
    public Formula simplify() {
        return this;
    }

    @Override
    public Formula getNegation() {
        return new Not(this).simplify();
    }
}
