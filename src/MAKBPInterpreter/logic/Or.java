package MAKBPInterpreter.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the disjunction of a set of formulas.
 */
public class Or implements Formula {
    /**
     * Set of inner formulas.
     */
    private Set<Formula> innerFormulas;

    /**
     * Constructor.
     * 
     * @param formulas collection of formulas
     */
    public Or(Collection<Formula> formulas) {
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
    public Or(Formula... formulas) {
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
                string += " v ";
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
        if (!(other instanceof Or)) {
            return false;
        }
        Or otherOr = (Or) other;

        if (otherOr.innerFormulas.size() != this.innerFormulas.size()) {
            return false;
        }

        for (Formula formula1 : this.innerFormulas) {
            boolean formula1IsFormula2 = false;
            for (Formula formula2 : otherOr.innerFormulas) {
                formula1IsFormula2 |= formula1.equals(formula2);
            }
            if (!formula1IsFormula2) {
                return false; // we didn't find formula1 in otherOr.innerFormulas set
            }
        }
        return true;
    }

    @Override
    public Formula simplify() {
        Set<Formula> formulas = new HashSet<>();
        for (Formula formula : this.innerFormulas) {
            if (formula instanceof Or) {
                Or or = (Or) formula;
                for (Formula orFormula : or.innerFormulas) {
                    formulas.add(orFormula.simplify());
                }
            } else {
                formulas.add(formula.simplify());
            }
        }
        return new Or(formulas);
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
        return new And(formulas).simplify();
    }
}
