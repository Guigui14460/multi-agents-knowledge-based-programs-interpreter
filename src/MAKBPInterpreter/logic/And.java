package MAKBPInterpreter.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the conjunction of a set of formulas.
 */
public class And implements Formula {
    /**
     * Set of inner formulas.
     */
    private Set<Formula> innerFormulas;

    /**
     * Constructor.
     * 
     * @param formulas collection of formulas
     */
    public And(Collection<Formula> formulas) {
        this.innerFormulas = new HashSet<>();
        for (Formula formula : formulas) {
            this.innerFormulas.add(formula.simplify());
        }
    }

    /**
     * Constructor.
     * 
     * @param formulas undifined number of formulas
     */
    public And(Formula... formulas) {
        this(Arrays.asList(formulas));
    }

    @Override
    public String toString() {
        String string = "";
        int size = this.innerFormulas.size();
        int current = 1;
        for (Formula formula : this.innerFormulas) {
            string += "(" + formula.toString() + ")";
            if (current < size) {
                string += " & ";
                current++;
            }
        }
        return string;
    }

    /**
     * Checks if the formulas are contained in the other set of formulas.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof And)) {
            return false;
        }
        And otherAnd = (And) other;

        if (otherAnd.innerFormulas.size() != this.innerFormulas.size()) {
            return false;
        }

        for (Formula formula1 : this.innerFormulas) {
            boolean formula1IsFormula2 = false;
            for (Formula formula2 : otherAnd.innerFormulas) {
                formula1IsFormula2 |= formula1.equals(formula2);
            }
            if (!formula1IsFormula2) {
                return false; // we didn't find formula1 in otherAnd.innerFormulas set
            }
        }
        return true;
    }

    @Override
    public Formula simplify() {
        Set<Formula> formulas = new HashSet<>();
        for (Formula formula : this.innerFormulas) {
            if (formula instanceof And) {
                And and = (And) formula;
                for (Formula andFormula : and.innerFormulas) {
                    formulas.add(andFormula.simplify());
                }
            } else {
                formulas.add(formula.simplify());
            }
        }
        return new And(formulas);
    }

    /**
     * Gets the inner formulas set object.
     * 
     * @return inner formulas set object
     * @see #innerFormulas
     */
    public Set<Formula> getInnerFormulas() {
        return this.innerFormulas;
    }

    @Override
    public Formula getNegation() {
        Set<Formula> formulas = new HashSet<>();
        for (Formula formula : this.innerFormulas) {
            formulas.add(formula.getNegation());
        }
        return new Or(formulas).simplify();
    }
}
