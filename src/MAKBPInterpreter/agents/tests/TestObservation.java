package MAKBPInterpreter.agents.tests;

import org.junit.Test;

import MAKBPInterpreter.agents.Observation;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.Observation} class.
 */
public class TestObservation extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.agents.Observation#Observation(Formula)}
     * method.
     */
    @Test
    public void testConstructor() {
        Formula formula = new Atom("test");
        Observation observation = new Observation(formula);

        assertNotNull(observation);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Observation#getFormula()} method.
     */
    @Test
    public void testGetters() {
        Formula formula = new Atom("test");
        Observation observation = new Observation(formula);

        assertEquals(formula, observation.getFormula());
    }
}
