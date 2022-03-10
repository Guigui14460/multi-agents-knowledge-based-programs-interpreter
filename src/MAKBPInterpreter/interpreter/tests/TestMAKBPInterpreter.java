package MAKBPInterpreter.interpreter.tests;

import static org.junit.Assert.assertNotEquals;
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
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.agents.exceptions.KripkeStructureInvalidRuntimeException;
import MAKBPInterpreter.agents.exceptions.NoKripkeWorldPossibleException;
import MAKBPInterpreter.interpreter.MAKBPInterpreter;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.interpreter.MAKBPInterpreter}
 * class.
 */
public class TestMAKBPInterpreter extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#MAKBPInterpreter(java.util.Set, MAKBPInterpreter.agents.KripkeStructure, java.util.Map, java.util.Map)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        Agent agentB = new Agent("b", new AgentProgram());

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        Map<Action, List<Object>> objects = new HashMap<>();

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, true, true);

        MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);

        assertNotNull("Must not be null", interpreter);
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#getStructures()} and
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#getPermissions()},
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#getObjects()}
     * methods.
     */
    @Test
    public void testGetters() {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        Agent agentB = new Agent("b", new AgentProgram());

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        Map<Action, List<Object>> objects = new HashMap<>();

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, true, true);

        MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);

        assertEquals("Must be equal", permissions, interpreter.getPermissions());
        assertEquals("Must be equal", objects, interpreter.getObjects());
        for (KripkeStructure struct : interpreter.getStructures().values()) {
            assertEquals("Must be equal", structure, struct);
        }

        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        graph.clear();
        structure = new KripkeStructure(graph, agents);

        assertEquals("Must be equal", permissions, interpreter.getPermissions());
        assertEquals("Must be equal", objects, interpreter.getObjects());
        for (KripkeStructure struct : interpreter.getStructures().values()) {
            assertNotEquals("Must not be equal", structure, struct);
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#publicAnnouncement(MAKBPInterpreter.agents.Agent, MAKBPInterpreter.logic.Formula)},
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#publicAnnouncement(java.util.Collection, MAKBPInterpreter.logic.Formula)}
     * and
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#publicAnnouncement(java.util.List, java.util.List)}
     * methods.
     * 
     * @throws Exception
     */
    @Test
    public void testPublicAnnouncement() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        Agent agentB = new Agent("b", new AgentProgram());

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);

        Formula formula = new Or(atom1, new Not(atom1));
        try {
            interpreter.publicAnnouncement(agents, formula);
        } catch (Exception e) {
            throw e;
        }

        for (KripkeStructure struct : interpreter.getStructures().values()) {
            assertEquals("Must be equal", structure, struct);
        }

        formula = atom1;
        try {
            interpreter.publicAnnouncement(agentA, formula);
        } catch (Exception e) {
            throw e;
        }

        for (Map.Entry<Agent, KripkeStructure> entry : interpreter.getStructures().entrySet()) {
            if (entry.getKey().equals(agentA)) {
                Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graphA = new HashMap<>();
                Map<Agent, Set<KripkeWorld>> nodeA = new HashMap<>();
                nodeA.put(agentA, new HashSet<>(Arrays.asList(world1)));
                nodeA.put(agentB, new HashSet<>(Arrays.asList(world1)));
                graphA.put(world1, new HashMap<>(nodeA));
                KripkeStructure struct = new KripkeStructure(graphA, agents);
                assertEquals("Must be equal", struct, entry.getValue());
            } else if (entry.getKey().equals(agentB)) {
                assertEquals("Must be equal", structure, entry.getValue());
            }
        }

        formula = new Not(atom1);
        try {
            interpreter.publicAnnouncement(agents, formula);
        } catch (Exception e) {
            throw e;
        }

        for (Map.Entry<Agent, KripkeStructure> entry : interpreter.getStructures().entrySet()) {
            if (entry.getKey().equals(agentA)) {
                Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graphA = new HashMap<>();
                KripkeStructure struct = new KripkeStructure(graphA, agents);
                assertEquals("Must be equal", struct, entry.getValue());
            } else if (entry.getKey().equals(agentB)) {
                Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graphB = new HashMap<>();
                Map<Agent, Set<KripkeWorld>> nodeB = new HashMap<>();
                nodeB.put(agentA, new HashSet<>(Arrays.asList(world2)));
                nodeB.put(agentB, new HashSet<>(Arrays.asList(world2)));
                graphB.put(world2, new HashMap<>(nodeB));
                KripkeStructure struct = new KripkeStructure(graphB, agents, false, false);
                assertEquals("Must be equal", struct, entry.getValue());
            }
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#getAssociatedAction(java.util.Collection, MAKBPInterpreter.agents.KripkeWorld)}
     * method.
     * 
     * @throws Exception
     */
    @Test
    public void testGetAssociatedAction() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        AgentKnowledge KaP = new AgentKnowledge(agentA, atom1);
        AgentKnowledge KaF = new AgentKnowledge(agentA, new Not(atom1));
        Incrementer incP_agentA = new Incrementer();
        Incrementer incF_agentA = new Incrementer();
        agentA.getProgram().put(KaP, incP_agentA);
        agentA.getProgram().put(KaF, incF_agentA);
        Agent agentB = new Agent("b", new AgentProgram());
        AgentKnowledge KbP = new AgentKnowledge(agentB, atom1);
        AgentKnowledge KbF = new AgentKnowledge(agentB, new Not(atom1));
        Incrementer incP_agentB = new Incrementer();
        Incrementer incF_agentB = new Incrementer();
        agentB.getProgram().put(KbP, incP_agentB);
        agentB.getProgram().put(KbF, incF_agentB);

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();
        objects.put(incP_agentA, new ArrayList<>(Arrays.asList(1)));
        objects.put(incF_agentA, new ArrayList<>(Arrays.asList(1)));
        objects.put(incP_agentB, new ArrayList<>(Arrays.asList(1)));
        objects.put(incF_agentB, new ArrayList<>(Arrays.asList(1)));

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        {
            MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
            KripkeWorld pointedWorld = world1;
            Map<Agent, Action> actions = new HashMap<>();
            actions.put(agentA, null);
            actions.put(agentB, incP_agentB);
            try {
                assertEquals("Must be equals", actions, interpreter.getAssociatedAction(agents, pointedWorld));
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;
                interpreter.publicAnnouncement(agentA, formula);
                Map<Agent, Action> actions = new HashMap<>();
                actions.put(agentA, incP_agentA);
                actions.put(agentB, incP_agentB);
                assertEquals("Must be equals", actions, interpreter.getAssociatedAction(agents, pointedWorld));
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world2;
                Formula formula = new Not(atom1);
                interpreter.publicAnnouncement(agentB, formula);
                Map<Agent, Action> actions = new HashMap<>();
                actions.put(agentA, null);
                actions.put(agentB, incF_agentB);
                assertEquals("Must be equals", actions, interpreter.getAssociatedAction(agents, pointedWorld));
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = new Not(atom1);
                interpreter.publicAnnouncement(agents, formula);
                assertThrows("Exception must be thrown", KripkeStructureInvalidRuntimeException.class,
                        () -> interpreter.getAssociatedAction(agents, pointedWorld));
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#executeAction(java.util.Map)}
     * method.
     *
     * @throws Exception
     */
    @Test
    public void testExecuteAction() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        AgentKnowledge KaP = new AgentKnowledge(agentA, atom1);
        AgentKnowledge KaF = new AgentKnowledge(agentA, new Not(atom1));
        Incrementer incP_agentA = new Incrementer();
        Incrementer incF_agentA = new Incrementer();
        Incrementer incNull_agentA = new Incrementer();
        agentA.getProgram().put(KaP, incP_agentA);
        agentA.getProgram().put(KaF, incF_agentA);
        agentA.getProgram().put(null, incNull_agentA);
        Agent agentB = new Agent("b", new AgentProgram());
        AgentKnowledge KbP = new AgentKnowledge(agentB, atom1);
        AgentKnowledge KbF = new AgentKnowledge(agentB, new Not(atom1));
        Incrementer incP_agentB = new Incrementer();
        Incrementer incF_agentB = new Incrementer();
        Incrementer incNull_agentB = new Incrementer();
        agentB.getProgram().put(KbP, incP_agentB);
        agentB.getProgram().put(KbF, incF_agentB);
        agentB.getProgram().put(null, incNull_agentB);

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();
        objects.put(incP_agentA, new ArrayList<>());
        objects.put(incF_agentA, new ArrayList<>());
        objects.put(incNull_agentA, new ArrayList<>());
        objects.put(incP_agentB, new ArrayList<>());
        objects.put(incF_agentB, new ArrayList<>());
        objects.put(incNull_agentB, new ArrayList<>());

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        {
            MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
            KripkeWorld pointedWorld = world1;
            try {
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                assertEquals(0, incP_agentA.getAcc());
                assertEquals(0, incF_agentA.getAcc());
                assertEquals(1, incNull_agentA.getAcc());
                assertEquals(1, incP_agentB.getAcc());
                assertEquals(0, incF_agentB.getAcc());
                assertEquals(0, incNull_agentB.getAcc());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;
                interpreter.publicAnnouncement(agentA, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                assertEquals(1, incP_agentA.getAcc());
                assertEquals(0, incF_agentA.getAcc());
                assertEquals(1, incNull_agentA.getAcc());
                assertEquals(2, incP_agentB.getAcc());
                assertEquals(0, incF_agentB.getAcc());
                assertEquals(0, incNull_agentB.getAcc());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world2;
                Formula formula = new Not(atom1);
                interpreter.publicAnnouncement(agentB, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                assertEquals(1, incP_agentA.getAcc());
                assertEquals(0, incF_agentA.getAcc());
                assertEquals(2, incNull_agentA.getAcc());
                assertEquals(2, incP_agentB.getAcc());
                assertEquals(1, incF_agentB.getAcc());
                assertEquals(0, incNull_agentB.getAcc());
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#reverseEngineering(java.util.Map)}
     * method.
     *
     * @throws Exception
     */
    @Test
    public void testReverseEngineering() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        AgentKnowledge KaP = new AgentKnowledge(agentA, atom1);
        AgentKnowledge KaF = new AgentKnowledge(agentA, new Not(atom1));
        Incrementer incP_agentA = new Incrementer();
        Incrementer incF_agentA = new Incrementer();
        Incrementer incNull_agentA = new Incrementer();
        agentA.getProgram().put(KaP, incP_agentA);
        agentA.getProgram().put(KaF, incF_agentA);
        agentA.getProgram().put(null, incNull_agentA);
        Agent agentB = new Agent("b", new AgentProgram());
        AgentKnowledge KbP = new AgentKnowledge(agentB, atom1);
        AgentKnowledge KbF = new AgentKnowledge(agentB, new Not(atom1));
        Incrementer incP_agentB = new Incrementer();
        Incrementer incF_agentB = new Incrementer();
        Incrementer incNull_agentB = new Incrementer();
        agentB.getProgram().put(KbP, incP_agentB);
        agentB.getProgram().put(KbF, incF_agentB);
        agentB.getProgram().put(null, incNull_agentB);

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();
        objects.put(incP_agentA, new ArrayList<>());
        objects.put(incF_agentA, new ArrayList<>());
        objects.put(incNull_agentA, new ArrayList<>());
        objects.put(incP_agentB, new ArrayList<>());
        objects.put(incF_agentB, new ArrayList<>());
        objects.put(incNull_agentB, new ArrayList<>());

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        {
            MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
            KripkeWorld pointedWorld = world1;
            try {
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = new HashMap<>();
                reversedEng.put(agentA, new And(new Not(KaP), new Not(KaF)));
                reversedEng.put(agentB, KbP);

                Map<Agent, Formula> results = interpreter.reverseEngineering(actions);
                for (Map.Entry<Agent, Formula> entry : results.entrySet()) {
                    assertEquals("Must be equals", reversedEng.get(entry.getKey()),
                            entry.getValue().simplify().simplify().simplify());
                }
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;
                interpreter.publicAnnouncement(agentA, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = new HashMap<>();
                reversedEng.put(agentA, KaP);
                reversedEng.put(agentB, KbP);

                Map<Agent, Formula> results = interpreter.reverseEngineering(actions);
                for (Map.Entry<Agent, Formula> entry : results.entrySet()) {
                    assertEquals("Must be equals", reversedEng.get(entry.getKey()),
                            entry.getValue().simplify().simplify().simplify());
                }
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world2;
                Formula formula = new Not(atom1);
                interpreter.publicAnnouncement(agentB, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = new HashMap<>();
                reversedEng.put(agentA, new And(new Not(KaP), new Not(KaF)));
                reversedEng.put(agentB, new And(KbF, KbP.getNegation()));

                Map<Agent, Formula> results = interpreter.reverseEngineering(actions);
                for (Map.Entry<Agent, Formula> entry : results.entrySet()) {
                    assertEquals("Must be equals", reversedEng.get(entry.getKey()),
                            entry.getValue().simplify().simplify().simplify());
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#reasoning(MAKBPInterpreter.agents.Agent, java.util.Map)}
     * method.
     *
     * @throws Exception
     */
    @Test
    public void testReasoning() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        AgentKnowledge KaP = new AgentKnowledge(agentA, atom1);
        AgentKnowledge KaF = new AgentKnowledge(agentA, new Not(atom1));
        Incrementer incP_agentA = new Incrementer();
        Incrementer incF_agentA = new Incrementer();
        Incrementer incNull_agentA = new Incrementer();
        agentA.getProgram().put(KaP, incP_agentA);
        agentA.getProgram().put(KaF, incF_agentA);
        agentA.getProgram().put(null, incNull_agentA);
        Agent agentB = new Agent("b", new AgentProgram());
        AgentKnowledge KbP = new AgentKnowledge(agentB, atom1);
        AgentKnowledge KbF = new AgentKnowledge(agentB, new Not(atom1));
        Incrementer incP_agentB = new Incrementer();
        Incrementer incF_agentB = new Incrementer();
        Incrementer incNull_agentB = new Incrementer();
        agentB.getProgram().put(KbP, incP_agentB);
        agentB.getProgram().put(KbF, incF_agentB);
        agentB.getProgram().put(null, incNull_agentB);

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();
        objects.put(incP_agentA, new ArrayList<>());
        objects.put(incF_agentA, new ArrayList<>());
        objects.put(incNull_agentA, new ArrayList<>());
        objects.put(incP_agentB, new ArrayList<>());
        objects.put(incF_agentB, new ArrayList<>());
        objects.put(incNull_agentB, new ArrayList<>());

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        {
            permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
            permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
            MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
            KripkeWorld pointedWorld = world1;
            try {
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                assertEquals("Must be equals", KbP,
                        interpreter.reasoning(agentA, reversedEng).simplify().simplify().simplify());
                assertEquals("Must be equals", new And(new Not(KaP), new Not(KaF)),
                        interpreter.reasoning(agentB, reversedEng).simplify().simplify().simplify());

                permissions.put(agentB, new HashSet<>());
                assertEquals("Must be equals", KbP,
                        interpreter.reasoning(agentA, reversedEng).simplify().simplify().simplify());
                assertEquals("Must be equals", new And(),
                        interpreter.reasoning(agentB, reversedEng).simplify().simplify().simplify());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
            permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;
                interpreter.publicAnnouncement(agentA, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                assertEquals("Must be equals", KbP,
                        interpreter.reasoning(agentA, reversedEng).simplify().simplify().simplify());
                assertEquals("Must be equals", KaP,
                        interpreter.reasoning(agentB, reversedEng).simplify().simplify().simplify());

                permissions.put(agentB, new HashSet<>(Arrays.asList(agentA, agentB)));
                assertEquals("Must be equals", new And(KaP, KbP),
                        interpreter.reasoning(agentB, reversedEng).simplify().simplify().simplify());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
            permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world2;
                Formula formula = new Not(atom1);
                interpreter.publicAnnouncement(agentB, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                assertEquals("Must be equals", new And(KbF, KbP.getNegation()),
                        interpreter.reasoning(agentA, reversedEng).simplify().simplify().simplify());
                assertEquals("Must be equals", new And(new Not(KaP), new Not(KaF)),
                        interpreter.reasoning(agentB, reversedEng).simplify().simplify().simplify());
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#isFinished(MAKBPInterpreter.agents.KripkeWorld)}
     * and
     * {@link MAKBPInterpreter.interpreter.MAKBPInterpreter#isFinished()}
     * methods.
     *
     * @throws Exception
     */
    @Test
    public void testIsFinished() throws Exception {
        Atom atom1 = new Atom("piece on heads");
        Agent agentA = new Agent("a", new AgentProgram());
        AgentKnowledge KaP = new AgentKnowledge(agentA, atom1);
        AgentKnowledge KaF = new AgentKnowledge(agentA, new Not(atom1));
        Incrementer incP_agentA = new Incrementer();
        Incrementer incF_agentA = new Incrementer();
        Incrementer incNull_agentA = new Incrementer();
        agentA.getProgram().put(KaP, incP_agentA);
        agentA.getProgram().put(KaF, incF_agentA);
        agentA.getProgram().put(null, incNull_agentA);
        Agent agentB = new Agent("b", new AgentProgram());
        AgentKnowledge KbP = new AgentKnowledge(agentB, atom1);
        AgentKnowledge KbF = new AgentKnowledge(agentB, new Not(atom1));
        Incrementer incP_agentB = new Incrementer();
        Incrementer incF_agentB = new Incrementer();
        Incrementer incNull_agentB = new Incrementer();
        agentB.getProgram().put(KbP, incP_agentB);
        agentB.getProgram().put(KbF, incF_agentB);
        agentB.getProgram().put(null, incNull_agentB);

        Set<Agent> agents = new HashSet<>(Arrays.asList(agentA, agentB));
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        permissions.put(agentA, new HashSet<>(Arrays.asList(agentB)));
        permissions.put(agentB, new HashSet<>(Arrays.asList(agentA)));
        Map<Action, List<Object>> objects = new HashMap<>();
        objects.put(incP_agentA, new ArrayList<>());
        objects.put(incF_agentA, new ArrayList<>());
        objects.put(incNull_agentA, new ArrayList<>());
        objects.put(incP_agentB, new ArrayList<>());
        objects.put(incF_agentB, new ArrayList<>());
        objects.put(incNull_agentB, new ArrayList<>());

        Map<Atom, Boolean> assignementWorld1 = new HashMap<>();
        assignementWorld1.put(atom1, true);
        Map<Atom, Boolean> assignementWorld2 = new HashMap<>();
        assignementWorld2.put(atom1, false);
        KripkeWorld world1 = new KripkeWorld(assignementWorld1);
        KripkeWorld world2 = new KripkeWorld(assignementWorld2);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> node = new HashMap<>();
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        node.put(agentB, new HashSet<>(Arrays.asList(world1)));
        graph.put(world1, new HashMap<>(node));
        node.put(agentB, new HashSet<>(Arrays.asList(world2)));
        node.put(agentA, new HashSet<>(Arrays.asList(world1, world2)));
        graph.put(world2, new HashMap<>(node));
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;

                interpreter.publicAnnouncement(agentA, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                interpreter.publicAnnouncement(agentA, interpreter.reasoning(agentA, reversedEng));

                assertTrue("Must be true", interpreter.isFinished(pointedWorld));
                assertFalse("Must be false", interpreter.isFinished());

                interpreter.publicAnnouncement(agents, formula);
                actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                reversedEng = interpreter.reverseEngineering(actions);
                interpreter.publicAnnouncement(agentA, interpreter.reasoning(agentA, reversedEng));
                interpreter.publicAnnouncement(agentB, interpreter.reasoning(agentB, reversedEng));

                assertTrue("Must be true", interpreter.isFinished(pointedWorld));
                assertTrue("Must be true", interpreter.isFinished());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;

                interpreter.publicAnnouncement(agents, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                interpreter.publicAnnouncement(agentA, interpreter.reasoning(agentA, reversedEng));
                interpreter.publicAnnouncement(agentB, interpreter.reasoning(agentB, reversedEng));

                interpreter.publicAnnouncement(agents, new Not(atom1));

                assertThrows("An exception must be thrown", NoKripkeWorldPossibleException.class,
                        () -> interpreter.isFinished(pointedWorld));
                assertThrows("An exception must be thrown",
                        NoKripkeWorldPossibleException.class, () -> interpreter.isFinished());
            } catch (Exception e) {
                throw e;
            }
        }

        {
            try {
                MAKBPInterpreter interpreter = new MAKBPInterpreter(agents, structure, permissions, objects);
                KripkeWorld pointedWorld = world1;
                Formula formula = atom1;

                interpreter.publicAnnouncement(agents, formula);
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, pointedWorld);
                interpreter.executeAction(actions);
                Map<Agent, Formula> reversedEng = interpreter.reverseEngineering(actions);
                interpreter.publicAnnouncement(agentA, interpreter.reasoning(agentA, reversedEng));
                interpreter.publicAnnouncement(agentB, interpreter.reasoning(agentB, reversedEng));

                KripkeWorld pointedWorld2 = world2;

                assertThrows("An exception must be thrown", KripkeStructureInvalidRuntimeException.class,
                        () -> interpreter.isFinished(pointedWorld2));
                assertTrue("Must be true", interpreter.isFinished());
            } catch (Exception e) {
                throw e;
            }
        }
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
