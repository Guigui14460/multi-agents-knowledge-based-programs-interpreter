package MAKBPInterpreter.agents.tests;

import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.AgentProgram} class.
 */
public class TestAgentProgram extends TestCase {
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
     * {@link MAKBPInterpreter.agents.AgentProgram#AgentProgram(List, List)},
     * {@link MAKBPInterpreter.agents.AgentProgram#AgentProgram(MAKBPInterpreter.agents.AgentProgram)}
     * and {@link MAKBPInterpreter.agents.AgentProgram#AgentProgram()} constructors.
     */
    @Test
    public void testConstructors() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        Formula formula3 = new Or(new Not(formula1), formula2);
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, formula3));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action3));

        AgentProgram program1 = new AgentProgram(formulas, actions);
        AgentProgram program2 = new AgentProgram();
        AgentProgram program3 = new AgentProgram(program1);

        assertNotNull(program1);
        assertNotNull(program2);
        assertNotNull(program3);

        assertEquals(formulas, program1.getOrderedKeys());
        assertEquals(actions, program1.getOrderedValues());
        assertEquals(new ArrayList<>(), program2.getOrderedKeys());
        assertEquals(new ArrayList<>(), program2.getOrderedValues());
        assertEquals(formulas, program3.getOrderedKeys());
        assertEquals(actions, program3.getOrderedValues());

        assertNotSame(formulas, program1.getOrderedKeys());
        assertNotSame(actions, program1.getOrderedValues());
        assertNotSame(formulas, program3.getOrderedKeys());
        assertNotSame(actions, program3.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2));
        List<Formula> formulas2 = new ArrayList<>();
        formulas2.add(null);
        List<Action> actions2 = new ArrayList<>();
        actions2.add(action3);

        AgentProgram program1 = new AgentProgram(formulas, actions);
        AgentProgram program2 = new AgentProgram(formulas2, actions2);
        AgentProgram program3 = new AgentProgram(program1);

        assertTrue(program1.equals(program3));
        assertFalse(program1.equals(program2));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#clear()} method.
     */
    @Test
    public void testClear() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action3));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(3, program.size());
        program.clear();
        assertEquals(0, program.size());
        assertEquals(new ArrayList<>(), program.getOrderedKeys());
        assertEquals(new ArrayList<>(), program.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#containsKey(Object)}
     * method.
     */
    @Test
    public void testContainsKey() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action3));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertTrue(program.containsKey(new Atom("a")));
        assertFalse(program.containsKey(new And(formula2, formula1)));
        assertTrue(program.containsKey(null));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#containsValue(Object)}
     * method.
     */
    @Test
    public void testContainsValue() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertTrue(program.containsValue(action2));
        assertFalse(program.containsValue(action3));
        assertTrue(program.containsValue(action1));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#entrySet()} method.
     */
    @Test
    public void testEntrySet() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        Set<Map.Entry<Formula, Action>> entries = program.entrySet();
        assertEquals(3, entries.size());
        for (Map.Entry<Formula, Action> entry : entries) {
            assertTrue(formulas.contains(entry.getKey()));
            assertTrue(actions.contains(entry.getValue()));
        }
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#get(Object)} method.
     */
    @Test
    public void testGet() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(action1, program.get(formula1));
        assertEquals(action2, program.get(formula2));
        assertEquals(action1, program.get(null));
        assertThrows(IndexOutOfBoundsException.class, () -> program.get(new And(formula1, formula2)));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getKey(int)} method.
     */
    @Test
    public void testGetKey() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(formula1, program.getKey(0));
        assertEquals(formula2, program.getKey(1));
        assertEquals(null, program.getKey(2));
        assertThrows(IndexOutOfBoundsException.class, () -> program.getKey(10));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getValue(int)} method.
     */
    @Test
    public void testGetValue() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(action1, program.getValue(0));
        assertEquals(action2, program.getValue(1));
        assertEquals(action1, program.getValue(2));
        assertThrows(IndexOutOfBoundsException.class, () -> program.getValue(10));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getIndex(Formula)}
     * method.
     */
    @Test
    public void testGetIndex() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(0, program.getIndex(formula1));
        assertEquals(1, program.getIndex(formula2));
        assertEquals(2, program.getIndex(null));
        assertEquals(-1, program.getIndex(new And(formula1, formula2)));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#isEmpty()} method.
     */
    @Test
    public void testIsEmpty() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);
        AgentProgram program2 = new AgentProgram();

        assertTrue(program2.isEmpty());
        assertFalse(program.isEmpty());
        program.clear();
        assertTrue(program.isEmpty());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#keySet()} method.
     */
    @Test
    public void testKeySet() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(new HashSet<>(formulas), program.keySet());
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.AgentProgram#insert(int, Formula, Action)}
     * method.
     */
    @Test
    public void testInsert() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1));

        AgentProgram program = new AgentProgram(formulas, actions);
        assertEquals(1, program.size());

        assertEquals(0, program.insert(0, formula2, action2));
        assertEquals(2, program.size());
        assertEquals(new ArrayList<>(Arrays.asList(formula2, formula1)), program.getOrderedKeys());
        assertEquals(new ArrayList<>(Arrays.asList(action2, action1)), program.getOrderedValues());

        assertEquals(2, program.insert(0, null, action1));
        assertEquals(3, program.size());
        assertEquals(new ArrayList<>(Arrays.asList(formula2, formula1, null)), program.getOrderedKeys());
        assertEquals(new ArrayList<>(Arrays.asList(action2, action1, action1)), program.getOrderedValues());

        assertEquals(1, program.insert(1, formula2, action2));
        assertEquals(3, program.size());
        assertEquals(new ArrayList<>(Arrays.asList(formula1, formula2, null)), program.getOrderedKeys());
        assertEquals(new ArrayList<>(Arrays.asList(action1, action2, action1)), program.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#put(Formula, Action)}
     * method.
     */
    @Test
    public void testPut() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram();
        assertEquals(0, program.size());
        program.put(formula1, action1);
        assertEquals(1, program.size());
        program.put(formula2, action2);
        assertEquals(2, program.size());
        program.put(null, action1);
        assertEquals(3, program.size());

        assertEquals(formulas, program.getOrderedKeys());
        assertEquals(actions, program.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#putAll(List, List)}
     * method.
     */
    @Test
    public void testPutAll() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram();
        assertEquals(0, program.size());
        program.putAll(formulas, actions);
        assertEquals(3, program.size());

        assertEquals(formulas, program.getOrderedKeys());
        assertEquals(actions, program.getOrderedValues());

        AgentProgram program2 = new AgentProgram();
        assertEquals(0, program2.size());
        program2.putAll(new ArrayList<>(Arrays.asList(formula1, formula2)),
                new ArrayList<>(Arrays.asList(action1, action2)));
        assertEquals(2, program2.size());
        program2.put(null, action1);
        assertEquals(3, program2.size());

        assertEquals(formulas, program2.getOrderedKeys());
        assertEquals(actions, program2.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#putAll(Map)} method.
     */
    @Test
    public void testOverridedPutAll() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertThrows(UnsupportedOperationException.class, () -> program.putAll(new HashMap<>()));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#remove(Object)} method.
     */
    @Test
    public void testRemove() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(action1, program.remove(formula1));
        assertEquals(2, program.size());
        assertEquals(new ArrayList<>(Arrays.asList(formula2, null)), program.getOrderedKeys());
        assertEquals(new ArrayList<>(Arrays.asList(action2, action1)), program.getOrderedValues());
        assertEquals(action1, program.remove(null));
        assertEquals(1, program.size());
        assertEquals(new ArrayList<>(Arrays.asList(formula2)), program.getOrderedKeys());
        assertEquals(new ArrayList<>(Arrays.asList(action2)), program.getOrderedValues());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#size()} method.
     */
    @Test
    public void testSize() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2));
        List<Formula> formulas2 = new ArrayList<>(formulas);
        formulas2.add(null);
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2));
        List<Action> actions2 = new ArrayList<>(actions);
        actions2.add(action1);

        AgentProgram program = new AgentProgram(formulas, actions);
        AgentProgram program2 = new AgentProgram(formulas2, actions2);

        assertEquals(2, program.size());
        assertEquals(3, program2.size());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#values()} method.
     */
    @Test
    public void testValues() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(new HashSet<>(actions), program.values());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getKeysBeforeAt(int)}
     * method.
     */
    @Test
    public void testGetKeysBeforeAt() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(new ArrayList<>(Arrays.asList(formula1)), program.getKeysBeforeAt(1));
        assertEquals(new ArrayList<>(Arrays.asList(formula1, formula2, null)), program.getKeysBeforeAt(3));
        assertEquals(new ArrayList<>(), program.getKeysBeforeAt(0));
        assertEquals(new ArrayList<>(Arrays.asList(formula1, formula2, null)), program.getKeysBeforeAt(30));
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getOrderedKeys()}
     * method.
     */
    @Test
    public void testGetOrderedKeys() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(formulas, program.getOrderedKeys());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.AgentProgram#getOrderedValues()}
     * method.
     */
    @Test
    public void testGetOrderedValues() {
        Formula formula1 = new Atom("a");
        Formula formula2 = new Atom("b");
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Arrays.asList(formula1, formula2, null));
        List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(action1, action2, action1));

        AgentProgram program = new AgentProgram(formulas, actions);

        assertEquals(actions, program.getOrderedValues());
    }
}
