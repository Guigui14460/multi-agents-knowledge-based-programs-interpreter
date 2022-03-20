package MAKBPInterpreter.logic;

/**
 * Represents the base of all components of the modal and epistemic logic
 * representation.
 */
public interface Formula {
    /**
     * Verifies if the current object formula and {@code other} formula are the
     * same.
     * 
     * @param other other formula to test the equality
     * @return result of the equality test
     */
    public boolean equals(Object other);

    /**
     * Simplifies the logic formula.
     * 
     * @return simplified formula
     */
    public Formula simplify();

    /**
     * Returns the negation of the current formula.
     * 
     * @return negation of formula
     */
    public Formula getNegation();

    /**
     * Returns {@code true} if this formula contains the specified formula.
     * 
     * @param otherFormula other formula whose presence in this formula is to be
     *                     tested
     * @return {@code true} if this formula contains the specified formula
     */
    public boolean contains(Formula otherFormula);

    /**
     * Evaluates a state that we passed into arguments.
     * 
     * @param assignment state to evaluate
     * @return result of the evaluation
     * @throws Exception throws when the formula not supported this operation or
     *                   expected object not given
     */
    public boolean evaluate(LogicAssignment assignment) throws Exception;
}
