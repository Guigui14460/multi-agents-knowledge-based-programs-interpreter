package MAKBPInterpreter.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the disjunction of a set of operands.
 */
public class Or implements Formula {
    /**
     * Set of operands.
     */
    private Set<Formula> operands;

    /**
     * Constructor with a collection of operands.
     * 
     * @param operands collection of operands
     */
    public Or(Collection<Formula> operands) {
        this.operands = new HashSet<>();
        for (Formula operand : operands) {
            if (operand == null) {
                continue;
            }
            this.operands.add(operand);
        }
    }

    /**
     * Constructor with operands array.
     * 
     * @param operands undifined number of operands
     * @see #Or(Collection)
     */
    public Or(Formula... operands) {
        this(Arrays.asList(operands));
    }

    @Override
    public String toString() {
        String string = "";
        int size = this.operands.size();
        int current = 1;
        for (Formula operand : this.operands) {
            string += "(" + operand.toString() + ")";
            if (current < size) {
                string += " v ";
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
        if (other == null)
            return false;
        if (!(other instanceof Or))
            return false;

        Or otherOr = (Or) other;

        if (otherOr.operands.size() != this.operands.size()) {
            return false;
        }

        for (Formula operand1 : this.operands) {
            boolean operand1IsOperand2 = false;
            for (Formula operand2 : otherOr.operands) {
                operand1IsOperand2 |= operand1.equals(operand2);
            }
            if (!operand1IsOperand2) {
                return false; // we didn't find operand1 in otherOr.operands set
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.operands);
    }

    @Override
    public Formula simplify() {
        if (this.operands.size() == 1) {
            for (Formula operand : this.operands) {
                return operand;
            }
        }

        Set<Formula> operands = new HashSet<>();
        for (Formula operand : this.operands) {
            if (operand instanceof Or) {
                Or or = (Or) operand;
                for (Formula orOperand : or.operands) {
                    operands.add(orOperand.simplify());
                }
            } else {
                operands.add(operand.simplify());
            }
        }
        return new Or(operands);
    }

    @Override
    public Formula getNegation() {
        Set<Formula> operands = new HashSet<>();
        for (Formula operand : this.operands) {
            operands.add(operand.getNegation());
        }
        return new And(operands).simplify();
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

    @Override
    public boolean evaluate(LogicAssignment assignment) throws Exception {
        boolean result = false;
        for (Formula formula : this.operands) {
            result = result || formula.evaluate(assignment);
        }
        return result;
    }
}
