package MAKBPInterpreter.agents.tests;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import MAKBPInterpreter.agents.Action;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.Action} class.
 */
public class TestAction extends TestCase {
    /**
     * Tests the {@link MAKBPInterpreter.agents.Action} constructor.
     */
    @Test
    public void testConstructor() {
        Action action1 = new Action() {
            @Override
            public Object performs(Object... objects) throws Exception {
                System.out.println("Ok");
                return null;
            }
        };
        Action action2 = new Incrementer();

        assertNotNull("Must not be null", action1);
        assertNotNull("Must not be null", action2);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Action#performs(Object...)} method.
     */
    @Test
    public void testPerforms() {
        Incrementer action = new Incrementer();

        assertEquals(0, action.getAcc());
        try {
            action.performs();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        assertEquals(1, action.getAcc());
        try {
            action.performs(19);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        assertEquals(20, action.getAcc());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.Action#performs(Object...)} method
     * when an exception is thrown.
     */
    @Test
    public void testPerformsWithException() {
        Incrementer action = new Incrementer();

        assertEquals(0, action.getAcc());
        assertThrows(Exception.class, () -> action.performs(12, 99));
        assertEquals(0, action.getAcc());
    }
}

final class Incrementer implements Action {
    private int acc = 0;

    @Override
    public Object performs(Object... objects) throws Exception {
        if (objects.length == 0) {
            this.acc++;
            return this.acc;
        }

        if (objects.length == 1) {
            Integer o = (Integer) objects[0];
            this.acc += o;
            return this.acc;
        }

        throw new IllegalArgumentException("Need to have 0 or 1 integer in arguments");
    }

    public int getAcc() {
        return this.acc;
    }
}