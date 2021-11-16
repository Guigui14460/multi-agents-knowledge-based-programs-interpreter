package MAKBPInterpreter.agents;

import MAKBPInterpreter.logic.Formula;

/**
 * Represents the a subset of an environment in one single formula.
 */
public class Observation {
    /**
     * Formula which describe a subset of an environment.
     */
    private Formula obsFormula;

    /**
     * Constructor.
     * 
     * @param obsFormula formula to associate to the observation
     */
    public Observation(Formula obsFormula) {
        this.obsFormula = obsFormula;
    }

    /**
     * Gets the formula corresponding to an observed environment.
     * 
     * @return formula
     */
    public Formula getFormula() {
        return this.obsFormula;
    }
}
