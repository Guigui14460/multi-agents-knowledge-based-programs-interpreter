package MAKBPInterpreter.logic.tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Atom} class.
 */
public class TestAtom extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.Atom#Atom(String)} constructor.
     */
    @Test
    public void testConstructor() {
        Atom atom1, atom2, atom3;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("a is muddy");
        atom3 = new Atom("b is muddy");

        assertNotNull("Must not be null", atom1);
        assertNotNull("Must not be null", atom2);
        assertNotNull("Must not be null", atom3);
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Atom#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2, atom3;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("a is muddy");
        atom3 = new Atom("b is muddy");

        assertTrue("Those formulas must be the same", atom1.equals(atom2));
        assertFalse("Those formulas must not be the same", atom1.equals(atom3));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Atom#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom1 = new Atom("a is muddy");

        assertSame("Those formulas must be the same", atom1, atom1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Atom#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1 = new Atom("a is muddy");
        Not not1 = new Not(atom1);

        assertNotSame("Those formulas must not be the same", not1, atom1.getNegation());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Atom#contains(Formula)} method.
     */
    @Test
    public void testContains() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("a is muddy");
        Atom atom3 = new Atom("b is muddy");

        assertTrue("The formula must contains the other formula", atom1.contains(atom2));
        assertFalse("The formula must not contains the other formula", atom1.contains(atom3));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.Atom#evaluate(java.util.Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, false);

        assertTrue("The atom must be true", atom1.evaluate(assignment));
        assertFalse("The atom must be false", atom2.evaluate(assignment));
        assertFalse("The atom must be false", atom3.evaluate(assignment));
    }
}
