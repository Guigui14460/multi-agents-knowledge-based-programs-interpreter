package MAKBPInterpreter.logic.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import MAKBPInterpreter.logic.PropositionalLogicAssignment;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.logic.Or} class.
 */
public class TestOr extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#Or(Formula...)} and
     * {@link MAKBPInterpreter.logic.Or#Or(java.util.Collection)} constructors.
     */
    @Test
    public void testConstructors() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Or or0 = new Or();
        Or or1 = new Or(atom1, atom2);
        Or or2 = new Or(atom2, atom1);
        List<Formula> list = new ArrayList<>();
        list.add(atom1);
        list.add(atom2);
        Or or1list = new Or(list);

        assertNotNull("Must not be null", or0);
        assertNotNull("Must not be null", or1);
        assertNotNull("Must not be null", or2);
        assertNotNull("Must not be null", or1list);
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#getOperands()} method.
     */
    @Test
    public void testGetOperands() {
        Atom atom = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Set<Formula> set = new HashSet<>();
        set.add(atom);
        set.add(atom2);

        Or or = new Or(atom, atom2);

        assertEquals("Must be equal", 2, or.getOperands().size());
        assertEquals("Must be equal", set, or.getOperands());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Atom atom1, atom2;
        atom1 = new Atom("a is muddy");
        atom2 = new Atom("b is muddy");

        Or or1 = new Or(atom1, atom2);
        Or or2 = new Or(atom2, atom1);

        assertTrue("Those formulas must be the same", or1.equals(or2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        Or or1 = new Or(atom, not1, not2);
        Set<Formula> simplifiedFormulas = new HashSet<>();
        simplifiedFormulas.add(atom);
        simplifiedFormulas.add(not1);
        simplifiedFormulas.add(atom);
        Or or2 = new Or(simplifiedFormulas);

        assertEquals("All inner formulas must be simplified", or2, or1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#simplify()} method. Specialized
     * when we have another "Or" formula inside the current "Or".
     */
    @Test
    public void testSimplify2() {
        Atom atom = new Atom("a is muddy");
        Not not1 = new Not(atom);
        Not not2 = new Not(not1);

        Or or1 = new Or(atom, new Or(not1, not2));
        Set<Formula> simplifiedFormulas = new HashSet<>();
        simplifiedFormulas.add(atom);
        simplifiedFormulas.add(not1);
        simplifiedFormulas.add(atom);
        Or or2 = new Or(simplifiedFormulas);

        assertEquals("All inner formulas must be simplified", or2, or1.simplify());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#getNegation()} method.
     */
    @Test
    public void testGetNegation() {
        Atom atom1 = new Atom("a is muddy");
        Not not1 = new Not(atom1);

        Or or1 = new Or(atom1, not1, new And(atom1, not1));
        And and1 = new And(not1, atom1, new Or(not1, atom1));

        assertEquals("Must be equal", and1, or1.getNegation());
    }

    /**
     * Tests the {@link MAKBPInterpreter.logic.Or#contains(Formula)} method.
     */
    @Test
    public void testContains() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");
        Or or1 = new Or(atom1, new Not(atom2));

        assertTrue("The formula must contains the other formula", or1.contains(atom1));
        assertTrue("The formula must contains the other formula", or1.contains(atom2));
        assertTrue("The formula must contains the other formula", or1.contains(new Not(atom2)));
        assertFalse("The formula must not contains the other formula", or1.contains(atom3));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.logic.Or#evaluate(MAKBPInterpreter.logic.LogicAssignment)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("a is muddy");
        Atom atom2 = new Atom("b is muddy");
        Atom atom3 = new Atom("c is muddy");
        Or or1 = new Or(atom1, atom2);
        Or or2 = new Or(atom2, atom3);

        Map<Atom, Boolean> assignmentMap = new HashMap<>();
        assignmentMap.put(atom1, true);
        assignmentMap.put(atom2, false);
        assignmentMap.put(atom3, false);
        PropositionalLogicAssignment assignment = new PropositionalLogicAssignment(assignmentMap);

        try {
            assertTrue("The atom must be true", or1.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
        try {
            assertFalse("The atom must be false", or2.evaluate(assignment));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}
