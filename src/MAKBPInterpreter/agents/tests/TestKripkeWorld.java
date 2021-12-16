package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.KripkeWorld} class.
 */
public class TestKripkeWorld extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeWorld#KripkeWorld(String, Map)} and
     * {@link MAKBPInterpreter.agents.KripkeWorld#KripkeWorld(Map)} methods
     * contructors.
     */
    @Test
    public void testConstructors() {
        KripkeWorld world1 = new KripkeWorld("test", new HashMap<>());
        KripkeWorld world2 = new KripkeWorld(new HashMap<>());

        assertNotNull(world1);
        assertNotNull(world2);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.KripkeWorld#getName()} and
     * {@link MAKBPInterpreter.agents.KripkeWorld#getAssignment()} methods.
     */
    @Test
    public void testGetters() {
        String name = "test";
        Map<Atom, Boolean> assignment = new HashMap<>();
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        assignment.put(atom1, true);
        assignment.put(atom2, false);

        KripkeWorld world = new KripkeWorld(name, new HashMap<>(assignment));

        assertEquals(name, world.getName());
        assertEquals(assignment, world.getAssignment());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.KripkeWorld#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        String name = "test";
        String name2 = "test2";
        Map<Atom, Boolean> assignment = new HashMap<>();
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        assignment.put(atom1, true);
        assignment.put(atom2, false);
        Map<Atom, Boolean> assignment2 = new HashMap<>(assignment);
        assignment2.put(atom1, false);

        KripkeWorld world1 = new KripkeWorld(name, assignment);
        KripkeWorld world2 = new KripkeWorld(name, new HashMap<>(assignment));
        KripkeWorld world3 = new KripkeWorld(name2, new HashMap<>(assignment));
        KripkeWorld world4 = new KripkeWorld(name2, new HashMap<>(assignment2));

        assertTrue(world1.equals(world2));
        assertFalse(world1.equals(world3));
        assertFalse(world1.equals(world4));
        assertFalse(world2.equals(world3));
        assertFalse(world2.equals(world4));
        assertFalse(world3.equals(world4));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeWorld#satisfied(Formula, KripkeStructure)}
     * method.
     */
    @Test
    public void testSatisfied() {
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Formula formula = new And(new Not(atom2), atom1);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, false);
        KripkeWorld world1 = new KripkeWorld("test", assignment);
        Map<Atom, Boolean> assignment2 = new HashMap<>(assignment);
        assignment2.put(atom1, false);
        KripkeWorld world2 = new KripkeWorld("test2", assignment2);

        Agent agent = new Agent("a", new HashMap<>());
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map1 = new HashMap<>();
        map1.put(agent, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world1, map1);
        Map<Agent, Set<KripkeWorld>> map2 = new HashMap<>();
        map2.put(agent, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, map2);
        KripkeStructure structure = new KripkeStructure(graph, Arrays.asList(agent));

        try {
            assertTrue(world1.satisfied(formula, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(world2.satisfied(formula, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            Formula formula2 = new And(new Not(atom2), atom1, new Atom("3"));
            assertFalse(world1.satisfied(formula2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
    }
}
