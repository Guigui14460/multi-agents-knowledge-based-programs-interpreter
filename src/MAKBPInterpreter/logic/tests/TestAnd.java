package MAKBPInterpreter.logic.tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.And} class.
 */
public class TestAnd extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.And#getInnerFormulas()} method.
     */
    @Test
    public void testGetInnerFormula() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Set<Formula> set = new HashSet<>();
        set.add(atom1);
        set.add(atom2);

        And and1 = new And(atom1, atom2);

        assertNotNull("Must not be null", and1);

        assertEquals("Must be equal", 2, and1.getInnerFormulas().size());
        assertEquals("Must be equal", set, and1.getInnerFormulas());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.And#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        And and1 = new And(atom1, atom2);
        And and2 = new And(atom2, atom1);

        assertNotNull("Must not be null", and1);
        assertNotNull("Must not be null", and2);

        assertTrue("Those formulas must be the same", and1.equals(and2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.And#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        And and1 = new And(atom, not1, not2);
        Set<Formula> simplifiedFormulas = new HashSet<>();
        simplifiedFormulas.add(atom);
        simplifiedFormulas.add(not1);
        simplifiedFormulas.add(atom);
        And and2 = new And(simplifiedFormulas);

        assertNotNull("Must not be null", and1);
        assertNotNull("Must not be null", and2);

        assertEquals("All inner formulas must be simplified", and2, and1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.And#simplify()} method. Specialized
     * when we have another "And" formula inside the current "And".
     */
    @Test
    public void testSimplify2() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        And and1 = new And(atom, new And(not1, not2));
        Set<Formula> simplifiedFormulas = new HashSet<>();
        simplifiedFormulas.add(atom);
        simplifiedFormulas.add(not1);
        simplifiedFormulas.add(atom);
        And and2 = new And(simplifiedFormulas);

        assertNotNull("Must not be null", and1);
        assertNotNull("Must not be null", and2);

        assertEquals("All inner formulas must be simplified", and2, and1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.And#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1 = new Atom("a is muddy");
        Not not1 = new Not(atom1);
        Not not2 = new Not(not1);

        And and1 = new And(atom1, not1, not2);
        Or or1 = new Or(not1, atom1, not1);

        assertNotNull("Must not be null", and1);
        assertNotNull("Must not be null", or1);

        assertEquals("Must be equal", and1, or1.getNegation());
    }
}
