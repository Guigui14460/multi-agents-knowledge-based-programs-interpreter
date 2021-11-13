package MAKBPInterpreter.logic.tests;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Implication;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Implication} class.
 */
public class TestImplication extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#getLeftOperand()} method.
     */
    @Test
    public void testGetLeftOperand() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Implication imply1 = new Implication(atom1, atom2);

        assertNotNull("Must not be null", imply1);

        assertEquals("Must be equal", atom1, imply1.getLeftOperand());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#getRightOperand()}
     * method.
     */
    @Test
    public void testGetRightOperand() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Implication imply1 = new Implication(atom1, atom2);

        assertNotNull("Must not be null", imply1);

        assertEquals("Must be equal", atom2, imply1.getRightOperand());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Implication imply1 = new Implication(atom1, atom2);
        Implication imply2 = new Implication(atom2, atom1);

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", imply2);

        assertFalse("Those formulas must not be the same", imply1.equals(imply2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Implication imply1 = new Implication(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new Or(atom1, atom2, new Not(atom1));

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", expected);

        assertEquals("All inner formulas must be simplified", expected, imply1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Implication imply1 = new Implication(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new And(new Not(atom1), new Not(atom2), atom1);

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", expected);

        assertEquals("Must be equal", expected, imply1.getNegation());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Implication#contains(Formula)}
     * method.
     */
    @Test
    public void testContains() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");
        Implication imply1 = new Implication(atom1, new Not(atom2));

        assertNotNull("Must not be null", atom1);
        assertNotNull("Must not be null", atom2);
        assertNotNull("Must not be null", atom3);
        assertNotNull("Must not be null", imply1);

        assertTrue("The formula must contains the other formula", imply1.contains(atom1));
        assertTrue("The formula must contains the other formula", imply1.contains(atom2));
        assertFalse("The formula must not contains the other formula", imply1.contains(atom3));
    }
}
