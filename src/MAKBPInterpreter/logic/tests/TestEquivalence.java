package MAKBPInterpreter.logic.tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Equivalence;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Equivalence} class.
 */
public class TestEquivalence extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.Equivalence#Equivalence(Formula, Formula)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Equivalence equivalence1 = new Equivalence(atom1, atom2);
        Equivalence equivalence2 = new Equivalence(atom2, atom1);

        assertNotNull("Must not be null", equivalence1);
        assertNotNull("Must not be null", equivalence2);
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#getLeftOperand()} method.
     */
    @Test
    public void testGetLeftOperand() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Equivalence equivalence1 = new Equivalence(atom1, atom2);

        assertEquals("Must be equal", atom1, equivalence1.getLeftOperand());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#getRightOperand()}
     * method.
     */
    @Test
    public void testGetRightOperand() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Equivalence equivalence1 = new Equivalence(atom1, atom2);

        assertEquals("Must be equal", atom2, equivalence1.getRightOperand());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Equivalence equivalence1 = new Equivalence(atom1, atom2);
        Equivalence equivalence2 = new Equivalence(atom2, atom1);

        assertFalse("Those formulas must not be the same", equivalence1.equals(equivalence2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Equivalence equivalence1 = new Equivalence(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new And(new Or(atom1, atom2, new Not(atom1)),
                new Or(new And(new Not(atom2), atom1), new Not(atom1)));
        assertNotNull("Must not be null", expected);

        assertEquals("All inner formulas must be simplified", expected, equivalence1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Equivalence equivalence1 = new Equivalence(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new Or(new And(atom1, new Not(atom2), new Not(atom1)),
                new And(new Or(atom2, new Not(atom1)), atom1));

        assertNotNull("Must not be null", expected);

        assertEquals("Must be equal", expected, equivalence1.getNegation());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Equivalence#contains(Formula)}
     * method.
     */
    @Test
    public void testContains() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");
        Equivalence equivalence1 = new Equivalence(atom1, new Not(atom2));

        assertTrue("The formula must contains the other formula", equivalence1.contains(atom1));
        assertTrue("The formula must contains the other formula", equivalence1.contains(atom2));
        assertFalse("The formula must not contains the other formula", equivalence1.contains(atom3));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.Equivalence#evaluate(java.util.Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");
        Equivalence equivalence1 = new Equivalence(atom1, atom2);
        Equivalence equivalence2 = new Equivalence(atom2, atom3);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, true);
        assignment.put(atom3, false);

        try {
            assertTrue("The atom must be true", equivalence1.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
        try {
            assertFalse("The atom must be false", equivalence2.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}
