package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.Box;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.Box} class.
 */
public class TestBox extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Box#Box(Agent, MAKBPInterpreter.logic.Formula)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Formula atom1 = new Atom("a is muddy");
        Agent a = new Agent("a", new AgentProgram());

        Box Ka = new Box(a, atom1);

        assertNotNull("Must be not null", Ka);
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Box#evaluate(Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Agent agent = new Agent("a", new AgentProgram());
        Box Ka = new Box(agent, atom1);
        Box Ka2 = new Box(agent, new Not(atom2));

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, false);
        KripkeWorld world1 = new KripkeWorld("test", assignment);
        Map<Atom, Boolean> assignment2 = new HashMap<>(assignment);
        assignment2.put(atom2, true);
        KripkeWorld world2 = new KripkeWorld("test2", assignment2);

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map1 = new HashMap<>();
        map1.put(agent, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world1, map1);
        Map<Agent, Set<KripkeWorld>> map2 = new HashMap<>();
        map2.put(agent, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, map2);
        KripkeStructure structure = new KripkeStructure(graph, Arrays.asList(agent));

        try {
            assertTrue(Ka.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(Ka2.evaluate(world2.getAssignment(), world2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
    }
}
