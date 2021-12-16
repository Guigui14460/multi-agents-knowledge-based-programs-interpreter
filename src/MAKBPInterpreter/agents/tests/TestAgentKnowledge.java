package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.AgentKnowledge} class.
 */
public class TestAgentKnowledge extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.AgentKnowledge#AgentKnowledge(Agent, MAKBPInterpreter.logic.Formula)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Formula atom1 = new Atom("a is muddy");
        Agent a = new Agent("a", new HashMap<>());

        AgentKnowledge Ka = new AgentKnowledge(a, atom1);

        assertNotNull("Must be not null", Ka);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentKnowledge#getAgent()} and
     * {@link MAKBPInterpreter.agents.AgentKnowledge#getInnerFormula()} methods.
     */
    @Test
    public void testGetters() {
        Agent agent = new Agent("a", new HashMap<>());
        Formula formula = new Atom("a is muddy");
        AgentKnowledge Ka = new AgentKnowledge(agent, formula);

        assertEquals(agent, Ka.getAgent());
        assertEquals(formula, Ka.getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentKnowledge#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Agent agent = new Agent("a", new HashMap<>());
        Formula formula = new Not(new Not(new Atom("a is muddy")));
        AgentKnowledge Ka = new AgentKnowledge(agent, formula);
        AgentKnowledge Ka_expected = new AgentKnowledge(agent, new Atom("a is muddy"));

        assertEquals(Ka_expected.getAgent(), ((AgentKnowledge) Ka.simplify()).getAgent());
        assertEquals(Ka_expected.getInnerFormula(), ((AgentKnowledge) Ka.simplify()).getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentKnowledge#getNegation()}
     * method.
     */
    @Test
    public void testGetNegation() {
        Agent agent = new Agent("a", new HashMap<>());
        Formula formula = new Not(new Atom("a is muddy"));
        Formula Ka = new AgentKnowledge(agent, formula);
        Formula Ka_expected1 = new Not(new AgentKnowledge(agent, formula));

        assertEquals(((Not) Ka_expected1).getOperand(),
                ((Not) Ka.getNegation()).getOperand());

        Formula Ka2 = new Not(new AgentKnowledge(agent, formula));
        Formula Ka_expected2 = new AgentKnowledge(agent, formula);
        assertEquals(((AgentKnowledge) Ka_expected2).getInnerFormula(),
                ((AgentKnowledge) Ka2.getNegation()).getInnerFormula());

    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentKnowledge#equals(Object)}
     * method.
     */
    @Test
    public void testEquals() {
        Agent agent = new Agent("a", new HashMap<>());
        Formula formula = new Atom("a is muddy");
        AgentKnowledge Ka1 = new AgentKnowledge(agent, formula);
        AgentKnowledge Ka2 = new AgentKnowledge(agent, new Not(formula));
        AgentKnowledge Ka3 = new AgentKnowledge(agent, formula);

        assertTrue(Ka1.equals(Ka3));
        assertFalse(Ka1.equals(Ka2));
        assertFalse(Ka2.equals(Ka3));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentKnowledge#contains(Formula)}
     * method.
     */
    @Test
    public void testContains() {
        Agent agent = new Agent("a", new HashMap<>());
        Formula one = new Atom("a is muddy");
        Formula two = new Not(one);
        Formula three = new Atom("b is muddy");
        AgentKnowledge Ka = new AgentKnowledge(agent, two);

        assertTrue(Ka.contains(one));
        assertFalse(Ka.contains(three));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.AgentKnowledge#evaluate(Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Agent agent = new Agent("a", new HashMap<>());
        AgentKnowledge Ka = new AgentKnowledge(agent, atom1);
        AgentKnowledge Ka2 = new AgentKnowledge(agent, new Not(atom2));

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
