package MAKBPInterpreter.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the conjunction of a set of operands.
 */
public class And implements Formula {
    /**
     * Set of operands.
     */
    private Set<Formula> operands;

    /**
     * Constructor with collection of operands.
     * 
     * @param operands collection of operands
     */
    public And(Collection<Formula> operands) {
        this.operands = new HashSet<>();
        for (Formula operand : operands) {
            this.operands.add(operand);
        }
    }

    /**
     * Constructor with operands array.
     * 
     * @param operands undifined number of operands
     */
    public And(Formula... operands) {
        this.operands = new HashSet<>();
        Collections.addAll(this.operands, operands);
    }

    @Override
    public String toString() {
        String string = "";
        int size = this.operands.size();
        int current = 1;
        for (Formula operand : this.operands) {
            string += "(" + operand.toString() + ")";
            if (current < size) {
                string += " & ";
                current++;
            }
        }
        return string;
    }

    /**
     * Checks if the operands are the same as the other set of operands.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof And))
            return false;

        And otherAnd = (And) other;

        if (otherAnd.operands.size() != this.operands.size()) {
            return false;
        }

        for (Formula operand1 : this.operands) {
            boolean operand1IsOperand2 = false;
            for (Formula operand2 : otherAnd.operands) {
                operand1IsOperand2 |= operand1.equals(operand2);
            }
            if (!operand1IsOperand2) {
                return false; // we didn't find operand1 in otherAnd.operands set
            }
        }
        return true;
    }

    @Override
    public Formula simplify() {
        Set<Formula> operands = new HashSet<>();
        for (Formula operand : this.operands) {
            if (operand instanceof And) {
                And and = (And) operand;
                for (Formula andFormula : and.operands) {
                    operands.add(andFormula.simplify());
                }
            } else {
                operands.add(operand.simplify());
            }
        }
        return new And(operands);
    }

    @Override
    public Formula getNegation() {
        Set<Formula> operands = new HashSet<>();
        for (Formula operand : this.operands) {
            operands.add(operand.getNegation());
        }
        return new Or(operands).simplify();
    }

    @Override
    public boolean contains(Formula otherFormula) {
        boolean contains = false;
        for (Formula operand : this.operands) {
            contains |= operand.contains(otherFormula);
        }
        return contains;
    }

    /**
     * Gets a copy of the operands set object.
     * 
     * @return operands set object
     * @see #operands
     */
    public Set<Formula> getOperands() {
        return new HashSet<>(this.operands);
    }
}
