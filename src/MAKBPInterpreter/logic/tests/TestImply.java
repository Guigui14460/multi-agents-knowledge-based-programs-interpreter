package MAKBPInterpreter.logic.tests;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Imply;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Imply} class.
 */
public class TestImply extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.Imply#getLeftFormula()} method.
     */
    @Test
    public void testGetLeftFormula() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Imply imply1 = new Imply(atom1, atom2);

        assertNotNull("Must not be null", imply1);

        assertEquals("Must be equal", atom1, imply1.getLeftFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Imply#getRightFormula()} method.
     */
    @Test
    public void testGetRightFormula() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        Imply imply1 = new Imply(atom1, atom2);

        assertNotNull("Must not be null", imply1);

        assertEquals("Must be equal", atom2, imply1.getRightFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Imply#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Imply imply1 = new Imply(atom1, atom2);
        Imply imply2 = new Imply(atom2, atom1);

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", imply2);

        assertFalse("Those formulas must not be the same", imply1.equals(imply2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Imply#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Imply imply1 = new Imply(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new Or(atom1, atom2, new Not(atom1));

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", expected);

        assertEquals("All inner formulas must be simplified", expected, imply1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Imply#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        Imply imply1 = new Imply(new Not(atom1), new Or(atom2, new Not(atom1)));

        Formula expected = new And(new Not(atom1), new Not(atom2), atom1);

        assertNotNull("Must not be null", imply1);
        assertNotNull("Must not be null", expected);

        assertEquals("Must be equal", expected, imply1.getNegation());
    }
}
