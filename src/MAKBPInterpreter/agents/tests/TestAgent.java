package MAKBPInterpreter.agents.tests;

import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.Observation;
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
     * Tests the {@link MAKBPInterpreter.agents.Agent#Agent(String, java.util.Map)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Agent agent = new Agent("agent 1", new HashMap<>());

        assertNotNull("Must not be null", agent);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#getName()} method.
     */
    @Test
    public void testGetName() {
        String name = "agent 1";
        Agent agent = new Agent(name, new HashMap<>());

        assertEquals("Must be equal", name, agent.getName());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#getConditions()} method.
     */
    @Test
    public void testGetConditions() {
        String name = "agent 1";
        Map<Formula, Action> conditions = new HashMap<>();
        Formula formula1 = new Atom("1 is muddy");
        conditions.put(formula1, action1);
        conditions.put(null, action2);
        Agent agent = new Agent(name, conditions);

        assertEquals("Must be equal", conditions, agent.getConditions());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Agent#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        String name = "agent 1";
        String name2 = "agent 2";
        Map<Formula, Action> conditions = new HashMap<>();
        Formula formula1 = new Atom("1 is muddy");
        conditions.put(formula1, action1);
        conditions.put(null, action2);
        Map<Formula, Action> conditionsCopy = new HashMap<>(conditions);
        Map<Formula, Action> conditions2 = new HashMap<>(conditions);
        Formula formula2 = new Atom("1 is muddy");
        conditions2.put(formula2, action1);
        Agent agent1 = new Agent(name, conditions);
        Agent agent2 = new Agent(name, conditionsCopy);
        Agent agent3 = new Agent(name2, conditionsCopy);
        Agent agent4 = new Agent(name2, conditions2);

        assertTrue("Must be equals", agent1.equals(agent2));
        assertFalse("Must not be equals", agent1.equals(agent3));
        assertFalse("Must not be equals", agent1.equals(agent4));
        assertFalse("Must not be equals", agent2.equals(agent3));
        assertFalse("Must not be equals", agent2.equals(agent4));
        assertFalse("Must not be equals", agent3.equals(agent4));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#getAssociatedAction(Observation)}
     * method.
     */
    @Test
    public void testGetAssociatedAction() {
        String name = "agent 1";
        Map<Formula, Action> conditions = new HashMap<>();
        Formula formula1 = new Atom("1 is muddy");
        conditions.put(formula1, action1);
        conditions.put(null, action2);
        Agent agent = new Agent(name, conditions);

        Observation observation1 = new Observation(formula1);
        assertEquals("Selected action is not correct", action1, agent.getAssociatedAction(observation1));

        Observation observation2 = new Observation(new Not(formula1));
        assertEquals("Selected action is not correct", action2, agent.getAssociatedAction(observation2));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#performsAssociatedAction(Observation, Object...)}
     * method.
     */
    @Test
    public void testPerformsAssociatedAction() {
        String name = "agent 1";
        Map<Formula, Action> conditions = new HashMap<>();
        Formula formula1 = new Atom("1 is muddy");
        conditions.put(formula1, action2);
        Agent agent = new Agent(name, conditions);

        Observation observation1 = new Observation(formula1);
        assertEquals(0, action2.getAcc());
        try {
            agent.performsAssociatedAction(observation1, 10);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        assertEquals(10, action2.getAcc());

        assertThrows(Exception.class, () -> agent.performsAssociatedAction(observation1, 10, 20));
        assertEquals(10, action2.getAcc());
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.Agent#performsAssociatedAction(Observation, Object...)}
     * method with a null action object.
     */
    @Test
    public void testPerformsAssociatedActionNullAction() {
        String name = "agent 1";
        Map<Formula, Action> conditions = new HashMap<>();
        Formula formula1 = new Atom("1 is muddy");
        Formula formula2 = new Not(formula1);
        conditions.put(formula1, action2);
        Agent agent = new Agent(name, conditions);

        Observation observation1 = new Observation(formula2);
        assertThrows(NullPointerException.class, () -> agent.performsAssociatedAction(observation1, 10, 20));
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

        Map<Formula, Action> conditions = new HashMap<>();
        conditions.put(formula2, action2);
        Agent agent = new Agent(name, conditions);

        Observation observation1 = new Observation(formula2);
        assertEquals("Observation must be retrieved", formula2, agent.reverseEngineering(action2));
        assertNull("Observation must be null", agent.reverseEngineering());
        agent.getAssociatedAction(observation1);
        assertEquals("Observation must be retrieved", formula2, agent.reverseEngineering());

        Map<Formula, Action> conditions2 = new HashMap<>(conditions);
        conditions2.put(formula1, action2);
        conditions2.put(formula3, action1);
        conditions2.put(null, action3);
        Agent agent2 = new Agent(name + "0", conditions2);

        assertNull(agent.reverseEngineering(null));
        assertEquals(new And(new Not(formula2), new Not(formula1), new Not(formula3)).simplify(),
                agent2.reverseEngineering(action3));
        assertEquals(new Or(formula1, formula2), agent2.reverseEngineering(action2));
    }
}
