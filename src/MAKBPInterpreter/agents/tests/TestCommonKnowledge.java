package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.CommonKnowledge;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.CommonKnowledge} class.
 */
public class TestCommonKnowledge extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.CommonKnowledge#CommonKnowledge(Formula, java.util.Set)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Formula atom1 = new Atom("a is muddy");
        Agent a = new Agent("a", new AgentProgram());

        CommonKnowledge CK = new CommonKnowledge(atom1, new HashSet<>(Arrays.asList(a)));

        assertNotNull("Must be not null", CK);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.CommonKnowledge#getAgents()} and
     * {@link MAKBPInterpreter.agents.CommonKnowledge#getInnerFormula()} methods.
     */
    @Test
    public void testGetters() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Atom("a is muddy");
        CommonKnowledge CK = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));

        assertEquals(new HashSet<>(Arrays.asList(agent, agent2)), CK.getAgents());
        assertEquals(formula, CK.getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.CommonKnowledge#simplify()} method.
     */
    @Test
    public void testSimplify() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Not(new Not(new Atom("a is muddy")));
        CommonKnowledge CK = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK_expected = new CommonKnowledge(new Atom("a is muddy"),
                new HashSet<>(Arrays.asList(agent, agent2)));

        assertEquals(CK_expected.getAgents(), ((CommonKnowledge) CK.simplify()).getAgents());
        assertEquals(CK_expected.getInnerFormula(), ((CommonKnowledge) CK.simplify()).getInnerFormula());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.CommonKnowledge#getNegation()}
     * method.
     */
    @Test
    public void testGetNegation() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Not(new Atom("a is muddy"));
        Formula CK = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        Formula CK_expected1 = new Not(new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2))));

        assertEquals(((Not) CK_expected1).getOperand(),
                ((Not) CK.getNegation()).getOperand());

        Formula CK2 = new Not(new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2))));
        Formula CK_expected2 = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        assertEquals(((CommonKnowledge) CK_expected2).getInnerFormula(),
                ((CommonKnowledge) CK2.getNegation()).getInnerFormula());

    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.CommonKnowledge#equals(Object)}
     * method.
     */
    @Test
    public void testEquals() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula formula = new Atom("a is muddy");
        CommonKnowledge CK1 = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK2 = new CommonKnowledge(new Not(formula), new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK3 = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK4 = new CommonKnowledge(formula, new HashSet<>(Arrays.asList(agent)));

        assertTrue(CK1.equals(CK3));
        assertFalse(CK1.equals(CK2));
        assertFalse(CK1.equals(CK4));
        assertFalse(CK2.equals(CK3));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.CommonKnowledge#contains(Formula)}
     * method.
     */
    @Test
    public void testContains() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Formula one = new Atom("a is muddy");
        Formula two = new Not(one);
        Formula three = new Atom("b is muddy");
        CommonKnowledge CK = new CommonKnowledge(two, new HashSet<>(Arrays.asList(agent, agent2)));

        assertTrue(CK.contains(one));
        assertFalse(CK.contains(three));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.CommonKnowledge#evaluate(Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluateLikeEverybodyKnowledge() {
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

        CommonKnowledge CK = new CommonKnowledge(atom1, new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK2 = new CommonKnowledge(new Not(atom2), new HashSet<>(Arrays.asList(agent, agent2)));
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

        CommonKnowledge CK3 = new CommonKnowledge(atom2, new HashSet<>(Arrays.asList(agent, agent2)));
        CommonKnowledge CK4 = new CommonKnowledge(atom1, new HashSet<>(Arrays.asList(agent, agent2)));
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

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.CommonKnowledge#evaluate(Map, Object...)}
     * method.
     */
    @Test
    public void testEvaluate() {
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Atom atom3 = new Atom("3");
        Atom atom4 = new Atom("4");
        Agent agent1 = new Agent("1", new AgentProgram());
        Agent agent2 = new Agent("2", new AgentProgram());
        Agent agent3 = new Agent("3", new AgentProgram());
        Agent agent4 = new Agent("4", new AgentProgram());

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put(atom1, true);
        assignment.put(atom2, false);
        assignment.put(atom3, true);
        assignment.put(atom4, true);
        KripkeWorld world1 = new KripkeWorld("1", assignment);
        Map<Atom, Boolean> assignment2 = new HashMap<>();
        assignment2.put(atom1, true);
        assignment2.put(atom2, true);
        assignment2.put(atom3, true);
        assignment2.put(atom4, true);
        KripkeWorld world2 = new KripkeWorld("2", assignment2);
        Map<Atom, Boolean> assignment3 = new HashMap<>();
        assignment3.put(atom1, true);
        assignment3.put(atom2, false);
        assignment3.put(atom3, false);
        assignment3.put(atom4, true);
        KripkeWorld world3 = new KripkeWorld("3", assignment3);
        Map<Atom, Boolean> assignment4 = new HashMap<>();
        assignment4.put(atom1, true);
        assignment4.put(atom2, true);
        assignment4.put(atom3, false);
        assignment4.put(atom4, false);
        KripkeWorld world4 = new KripkeWorld("4", assignment4);

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map1 = new HashMap<>();
        map1.put(agent1, new HashSet<>(Arrays.asList(world1)));
        map1.put(agent2, new HashSet<>(Arrays.asList(world1, world2)));
        map1.put(agent3, new HashSet<>(Arrays.asList(world1, world3)));
        map1.put(agent4, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, map1);
        Map<Agent, Set<KripkeWorld>> map2 = new HashMap<>();
        map2.put(agent1, new HashSet<>(Arrays.asList(world2)));
        map2.put(agent2, new HashSet<>(Arrays.asList(world2, world1)));
        map2.put(agent3, new HashSet<>(Arrays.asList(world2, world4)));
        map2.put(agent4, new HashSet<>(Arrays.asList(world2, world4)));
        graph.put(world2, map2);
        Map<Agent, Set<KripkeWorld>> map3 = new HashMap<>();
        map3.put(agent1, new HashSet<>(Arrays.asList(world3)));
        map3.put(agent2, new HashSet<>(Arrays.asList(world3, world4)));
        map3.put(agent3, new HashSet<>(Arrays.asList(world3, world1)));
        map3.put(agent4, new HashSet<>(Arrays.asList(world3, world4)));
        graph.put(world3, map3);
        Map<Agent, Set<KripkeWorld>> map4 = new HashMap<>();
        map4.put(agent1, new HashSet<>(Arrays.asList(world4)));
        map4.put(agent2, new HashSet<>(Arrays.asList(world4, world3)));
        map4.put(agent3, new HashSet<>(Arrays.asList(world4, world2)));
        map4.put(agent4, new HashSet<>(Arrays.asList(world4, world2, world3)));
        graph.put(world4, map4);
        KripkeStructure structure = new KripkeStructure(graph, Arrays.asList(agent1, agent2, agent3, agent4));

        CommonKnowledge CK = new CommonKnowledge(atom1, new HashSet<>(Arrays.asList(agent1, agent2, agent3, agent4)));
        try {
            assertTrue(CK.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertTrue(CK.evaluate(world2.getAssignment(), world2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        CommonKnowledge CK2 = new CommonKnowledge(atom4, new HashSet<>(Arrays.asList(agent1, agent2, agent3, agent4)));
        try {
            assertFalse(CK2.evaluate(world1.getAssignment(), world1, structure));
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

        CommonKnowledge CK3 = new CommonKnowledge(new And(atom1, atom4),
                new HashSet<>(Arrays.asList(agent1, agent2, agent3, agent4)));
        try {
            assertFalse(CK3.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK3.evaluate(world3.getAssignment(), world3, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        CommonKnowledge CK4 = new CommonKnowledge(atom4, new HashSet<>(Arrays.asList(agent1, agent3)));
        try {
            assertTrue(CK4.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK4.evaluate(world2.getAssignment(), world2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertTrue(CK4.evaluate(world3.getAssignment(), world3, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        CommonKnowledge CK5 = new CommonKnowledge(atom4, new HashSet<>(Arrays.asList(agent4)));
        try {
            assertTrue(CK5.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK5.evaluate(world2.getAssignment(), world2, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK5.evaluate(world4.getAssignment(), world4, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        CommonKnowledge CK6 = new CommonKnowledge(new Not(atom2), new HashSet<>(Arrays.asList(agent3)));
        try {
            assertTrue(CK6.evaluate(world1.getAssignment(), world1, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
        try {
            assertFalse(CK6.evaluate(world4.getAssignment(), world4, structure));
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
    }
}
