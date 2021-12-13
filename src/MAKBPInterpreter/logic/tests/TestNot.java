package MAKBPInterpreter.logic.tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Not} class.
 */
public class TestNot extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#Not(Formula)} constructor.
     */
    @Test
    public void testConstructor() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        assertNotNull("Must not be null", not1);
        assertNotNull("Must not be null", not2);
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#getOperand()} method.
     */
    @Test
    public void testGetOperand() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        assertSame("Those formulas must be the same", atom, not1.getOperand());
        assertSame("Those formulas must be the same", not1, not2.getOperand());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2, atom3;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("a is muddy");
        atom3 = new Atom("b is muddy");

        Not not1 = new Not(atom1);
        Not not2 = new Not(atom2);
        Not not3 = new Not(atom3);

        assertTrue("Those formulas must be the same", not1.equals(not2));
        assertFalse("Those formulas must not be the same", not1.equals(not3));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        assertSame("A negation simplification must return itself", not1, not1.simplify());
        assertSame("A double negation simplification must return the inner formula of the inner not", atom,
                not2.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1 = new Atom("a is muddy");
        Not not1 = new Not(atom1);

        assertEquals("Must be equal", atom1, not1.getNegation());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Not#contains(Formula)} method.
     */
    @Test
    public void testContains() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Not not1 = new Not(atom1);

        assertTrue("The formula must contains the other formula", not1.contains(atom1));
        assertFalse("The formula must not contains the other formula", not1.contains(atom2));
        assertFalse("The formula must not contains the other formula", not1.contains(new Not(atom2)));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.Not#evaluate(java.util.Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Not not1 = new Not(atom1);
        Not not2 = new Not(atom2);
        Not not3 = new Not(not1);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, false);

        try {
            assertFalse("The atom must be false", not1.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
        try {
            assertTrue("The atom must be true", not2.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
        try {
            assertTrue("The atom must be true", not3.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}
