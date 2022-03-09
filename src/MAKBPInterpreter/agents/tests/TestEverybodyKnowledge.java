package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.EverybodyKnowledge;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.EverybodyKnowledge} class.
 */
public class TestEverybodyKnowledge extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.EverybodyKnowledge#EverybodyKnowledge(Formula, java.util.Set)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Formula atom1 = new Atom("a is muddy");
        Agent a = new Agent("a", new AgentProgram());

        EverybodyKnowledge CK = new EverybodyKnowledge(atom1, new HashSet<>(Arrays.asList(a)));

        assertNotNull("Must be not null", CK);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.EverybodyKnowledge#getAgents()} and
     * {@link MAKBPInterpreter.agents.EverybodyKnowledge#getInnerFormula()} methods.
     */
    @Test
    public void testGetters() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Atom("a is muddy");
        EverybodyKnowledge CK = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));

        assertEquals(new HashSet<>(Arrays.asList(agent, agent2)), CK.getAgents());
        assertEquals(formula, CK.getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.EverybodyKnowledge#simplify()}
     * method.
     */
    @Test
    public void testSimplify() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Not(new Not(new Atom("a is muddy")));
        EverybodyKnowledge CK = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK_expected = new EverybodyKnowledge(new Atom("a is muddy"),
                new HashSet<>(Arrays.asList(agent, agent2)));

        assertEquals(CK_expected.getAgents(), ((EverybodyKnowledge) CK.simplify()).getAgents());
        assertEquals(CK_expected.getInnerFormula(), ((EverybodyKnowledge) CK.simplify()).getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.EverybodyKnowledge#getNegation()}
     * method.
     */
    @Test
    public void testGetNegation() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Not(new Atom("a is muddy"));
        Formula CK = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        Formula CK_expected1 = new Not(new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2))));

        assertEquals(((Not) CK_expected1).getOperand(),
                ((Not) CK.getNegation()).getOperand());

        Formula CK2 = new Not(new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2))));
        Formula CK_expected2 = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        assertEquals(((EverybodyKnowledge) CK_expected2).getInnerFormula(),
                ((EverybodyKnowledge) CK2.getNegation()).getInnerFormula());

    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.EverybodyKnowledge#equals(Object)}
     * method.
     */
    @Test
    public void testEquals() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Atom("a is muddy");
        EverybodyKnowledge CK1 = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK2 = new EverybodyKnowledge(new Not(formula), new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK3 = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK4 = new EverybodyKnowledge(formula, new HashSet<>(Arrays.asList(agent)));

        assertTrue(CK1.equals(CK3));
        assertFalse(CK1.equals(CK2));
        assertFalse(CK1.equals(CK4));
        assertFalse(CK2.equals(CK3));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.EverybodyKnowledge#contains(Formula)}
     * method.
     */
    @Test
    public void testContains() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula one = new Atom("a is muddy");
        Formula two = new Not(one);
        Formula three = new Atom("b is muddy");
        EverybodyKnowledge CK = new EverybodyKnowledge(two, new HashSet<>(Arrays.asList(agent, agent2)));

        assertTrue(CK.contains(one));
        assertFalse(CK.contains(three));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.EverybodyKnowledge#evaluate(Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());

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
        map1.put(agent2, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world1, map1);
        Map<Agent, Set<KripkeWorld>> map2 = new HashMap<>();
        map2.put(agent, new HashSet<>(Arrays.asList(world1, world2)));
        map2.put(agent2, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, map2);
        KripkeStructure structure = new KripkeStructure(graph, Arrays.asList(agent, agent2));

        EverybodyKnowledge CK = new EverybodyKnowledge(atom1, new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK2 = new EverybodyKnowledge(new Not(atom2), new HashSet<>(Arrays.asList(agent, agent2)));
        try {
            assertTrue(CK.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK2.evaluate(world2.getAssignment(), world2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph2 = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map3 = new HashMap<>();
        map3.put(agent2, new HashSet<>(Arrays.asList(world1, world2)));
        graph2.put(world1, map3);
        Map<Agent, Set<KripkeWorld>> map4 = new HashMap<>();
        map4.put(agent2, new HashSet<>(Arrays.asList(world1, world2)));
        graph2.put(world2, map4);
        KripkeStructure structure2 = new KripkeStructure(graph2, Arrays.asList(agent, agent2));

        EverybodyKnowledge CK3 = new EverybodyKnowledge(atom2, new HashSet<>(Arrays.asList(agent, agent2)));
        EverybodyKnowledge CK4 = new EverybodyKnowledge(atom1, new HashSet<>(Arrays.asList(agent, agent2)));
        try {
            assertFalse(CK3.evaluate(world1.getAssignment(), world1, structure2));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK3.evaluate(world2.getAssignment(), world2, structure2));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertTrue(CK4.evaluate(world1.getAssignment(), world1, structure2));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertTrue(CK4.evaluate(world2.getAssignment(), world2, structure2));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
    }
}
