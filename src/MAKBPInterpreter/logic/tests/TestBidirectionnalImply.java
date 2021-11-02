package MAKBPInterpreter.logic.tests;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.BidirectionnalImply;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.BidirectionnalImply} class.
 */
public class TestBidirectionnalImply extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.BidirectionnalImply#getLeftFormula()}
     * method.
     */
    @Test
    public void testGetLeftFormula() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        BidirectionnalImply bidirectionnalImply1 = new BidirectionnalImply(atom1, atom2);

        assertNotNull("Must not be null", bidirectionnalImply1);

        assertEquals("Must be equal", atom1, bidirectionnalImply1.getLeftFormula());
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.BidirectionnalImply#getRightFormula()} method.
     */
    @Test
    public void testGetRightFormula() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");

        BidirectionnalImply bidirectionnalImply1 = new BidirectionnalImply(atom1, atom2);

        assertNotNull("Must not be null", bidirectionnalImply1);

        assertEquals("Must be equal", atom2, bidirectionnalImply1.getRightFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.BidirectionnalImply#equals(Object)}
     * method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        BidirectionnalImply bidirectionnalImply1 = new BidirectionnalImply(atom1, atom2);
        BidirectionnalImply bidirectionnalImply2 = new BidirectionnalImply(atom2, atom1);

        assertNotNull("Must not be null", bidirectionnalImply1);
        assertNotNull("Must not be null", bidirectionnalImply2);

        assertFalse("Those formulas must not be the same", bidirectionnalImply1.equals(bidirectionnalImply2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.BidirectionnalImply#simplify()}
     * method.
     */
    @Test
    public void testSimplify() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        BidirectionnalImply bidirectionnalImply1 = new BidirectionnalImply(new Not(atom1),
                new Or(atom2, new Not(atom1)));

        Formula expected = new And(new Or(atom1, atom2, new Not(atom1)),
                new Or(new And(new Not(atom2), atom1), new Not(atom1)));

        assertNotNull("Must not be null", bidirectionnalImply1);
        assertNotNull("Must not be null", expected);

        assertEquals("All inner formulas must be simplified", expected, bidirectionnalImply1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.BidirectionnalImply#getNegation()}
     * method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");
        BidirectionnalImply bidirectionnalImply1 = new BidirectionnalImply(new Not(atom1),
                new Or(atom2, new Not(atom1)));

        Formula expected = new Or(new And(atom1, new Not(atom2), new Not(atom1)),
                new And(new Or(atom2, new Not(atom1)), atom1));

        assertNotNull("Must not be null", bidirectionnalImply1);
        assertNotNull("Must not be null", expected);

        assertEquals("Must be equal", expected, bidirectionnalImply1.getNegation());
    }
}
