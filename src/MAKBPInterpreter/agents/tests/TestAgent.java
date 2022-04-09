package MAKBPInterpreter.agents.tests;

import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.Agent} class.
 */
public class TestAgent extends TestCase {
    final static Action action1 = new Action() {
        @Override
        public Object performs(Object... objects) throws Exception {
            return null;
        }
    };
    final static Incrementer action2 = new Incrementer();
    final static Action action3 = new Action() {
        @Override
        public Object performs(Object... objects) throws Exception {
            return null;
        }
    };

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#Agent(String, MAKBPInterpreter.agents.AgentProgram)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Agent agent = new Agent("agent 1", new AgentProgram());

        assertNotNull("Must not be null", agent);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#getName()} method.
     */
    @Test
    public void testGetName() {
        String name = "agent 1";
        Agent agent = new Agent(name, new AgentProgram());

        assertEquals("Must be equal", name, agent.getName());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#getProgram()} method.
     */
    @Test
    public void testGetConditions() {
        String name = "agent 1";
        AgentProgram program = new AgentProgram();
        Formula formula1 = new Atom("1 is muddy");
        program.put(formula1, action1);
        program.put(null, action2);
        Agent agent = new Agent(name, program);

        assertEquals("Must be equal", program, agent.getProgram());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        String name = "agent 1";
        String name2 = "agent 2";
        AgentProgram program = new AgentProgram();
        Formula formula1 = new Atom("1 is muddy");
        program.put(formula1, action1);
        program.put(null, action2);
        AgentProgram programCopy = new AgentProgram(program);
        AgentProgram program2 = new AgentProgram(program);
        Formula formula2 = new Atom("1 is muddy");
        program2.put(formula2, action2);
        Agent agent1 = new Agent(name, program);
        Agent agent2 = new Agent(name, programCopy);
        Agent agent3 = new Agent(name2, programCopy);
        Agent agent4 = new Agent(name2, program2);

        assertTrue("Must be equals", agent1.equals(agent2));
        assertFalse("Must not be equals", agent1.equals(agent3));
        assertFalse("Must not be equals", agent1.equals(agent4));
        assertFalse("Must not be equals", agent2.equals(agent3));
        assertFalse("Must not be equals", agent2.equals(agent4));
        assertFalse("Must not be equals", agent3.equals(agent4));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#getAssociatedAction(MAKBPInterpreter.agents.KripkeStructure, MAKBPInterpreter.agents.KripkeWorld)}
     * method.
     */
    @Test
    public void testGetAssociatedAction() {
        String name = "agent 1";
        AgentProgram program = new AgentProgram();
        Formula formula1 = new Atom("1 is muddy");
        program.put(formula1, action1);
        program.put(null, action2);
        Agent agent = new Agent(name, program);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put((Atom) formula1, true);
        KripkeWorld world = new KripkeWorld(assignment);
        Set<Agent> agents = new HashSet<>(Arrays.asList(agent));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> relations = new HashMap<>();
        relations.put(agent, new HashSet<>(Arrays.asList(world)));
        graph.put(world, relations);
        KripkeStructure structure = new KripkeStructure(graph, agents);

        try {
            assertEquals("Selected action is not correct", action1, agent.getAssociatedAction(structure, world));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assignment.put((Atom) formula1, false);
        try {
            assertEquals("Selected action is not correct", action2, agent.getAssociatedAction(structure, world));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#performsAssociatedAction(MAKBPInterpreter.agents.KripkeStructure, MAKBPInterpreter.agents.KripkeWorld, Object...)}
     * method.
     */
    @Test
    public void testPerformsAssociatedAction() {
        String name = "agent 1";
        AgentProgram program = new AgentProgram();
        Formula formula1 = new Atom("1 is muddy");
        program.put(formula1, action2);
        Agent agent = new Agent(name, program);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put((Atom) formula1, true);
        KripkeWorld world = new KripkeWorld(assignment);
        Set<Agent> agents = new HashSet<>(Arrays.asList(agent));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> relations = new HashMap<>();
        relations.put(agent, new HashSet<>(Arrays.asList(world)));
        graph.put(world, relations);
        KripkeStructure structure = new KripkeStructure(graph, agents);

        assertEquals(0, action2.getAcc());
        try {
            agent.performsAssociatedAction(structure, world, 10);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        assertEquals(10, action2.getAcc());

        assertThrows(Exception.class, () -> agent.performsAssociatedAction(structure, world,
                10, 20));
        assertEquals(10, action2.getAcc());
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#performsAssociatedAction(MAKBPInterpreter.agents.KripkeStructure, MAKBPInterpreter.agents.KripkeWorld, Object...)}
     * method with a null action object.
     */
    @Test
    public void testPerformsAssociatedActionNullAction() {
        String name = "agent 1";
        AgentProgram program = new AgentProgram();
        Formula formula1 = new Atom("1 is muddy");
        program.put(formula1, action2);
        Agent agent = new Agent(name, program);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put((Atom) formula1, false);
        KripkeWorld world = new KripkeWorld(assignment);
        Set<Agent> agents = new HashSet<>(Arrays.asList(agent));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> relations = new HashMap<>();
        relations.put(agent, new HashSet<>(Arrays.asList(world)));
        graph.put(world, relations);
        KripkeStructure structure = new KripkeStructure(graph, agents);

        assertThrows(NullPointerException.class, () -> agent.performsAssociatedAction(structure, world, 10, 20));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#reverseEngineering(Action)} and
     * {@link MAKBPInterpreter.agents.Agent#reverseEngineering()}
     * methods.
     */
    @Test
    public void testReverseEngineeringMethods() {
        String name = "agent 1";
        Formula formula1 = new Atom("1 is muddy");
        Formula formula2 = new Not(formula1);
        Formula formula3 = new Atom("2 is muddy");

        AgentProgram program = new AgentProgram();
        program.put(formula2, action2);
        Agent agent = new Agent(name, program);

        Map<Atom, Boolean> assignment = new HashMap<>();
        assignment.put((Atom) formula1, false);
        assignment.put((Atom) formula3, false);
        KripkeWorld world = new KripkeWorld(assignment);
        Set<Agent> agents = new HashSet<>(Arrays.asList(agent));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> relations = new HashMap<>();
        relations.put(agent, new HashSet<>(Arrays.asList(world)));
        graph.put(world, relations);
        KripkeStructure structure = new KripkeStructure(graph, agents);

        assertEquals("Observation must be retrieved", formula2,
                agent.reverseEngineering(action2).simplify().simplify().simplify());
        assertNull("Observation must be null", agent.reverseEngineering());
        try {
            agent.getAssociatedAction(structure, world);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Observation must be retrieved", formula2,
                agent.reverseEngineering().simplify().simplify().simplify());

        AgentProgram program2 = new AgentProgram(program);
        program2.put(formula1, action2);
        program2.put(formula3, action1);
        program2.put(null, action3);
        Agent agent2 = new Agent(name + "0", program2);

        assertNull(agent.reverseEngineering(null));
        assertEquals(
                new And(formula2.getNegation(), formula1.getNegation(), formula3.getNegation()).simplify().simplify()
                        .simplify(),
                agent2.reverseEngineering(action3).simplify().simplify().simplify());
        assertEquals(new Or(formula1, formula2),
                agent2.reverseEngineering(action2).simplify().simplify().simplify());

        AgentProgram program3 = new AgentProgram(program);
        program3.put(formula1, action2);
        program3.put(formula3, action1);
        program3.put(null, action2);
        Agent agent3 = new Agent(name + "1", program3);

        assertNull(agent.reverseEngineering(null));
        assertEquals(new And(formula3, formula1.getNegation(), formula2.getNegation()),
                agent3.reverseEngineering(action1).simplify().simplify().simplify());
        assertEquals(
                new Or(formula2, new And(formula2.getNegation(), formula1),
                        new And(formula1.getNegation(), formula2.getNegation(), formula3.getNegation())).simplify()
                        .simplify().simplify(),
                agent3.reverseEngineering(action2).simplify().simplify().simplify());
    }
}
