package MAKBPInterpreter.agents.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import junit.framework.TestCase;

/**
 * Test class for the {@link MAKBPInterpreter.agents.KripkeStructure} class.
 */
public class TestKripkeStructure extends TestCase {
    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeStructure#KripkeStructure(java.util.Map, java.util.Collection)}
     * and
     * {@link MAKBPInterpreter.agents.KripkeStructure#KripkeStructure(java.util.Map, java.util.Collection, boolean, boolean)}
     * constructors.
     */
    @Test
    public void testConstructors() {
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Collection<Agent> agents = new HashSet<>();

        KripkeStructure structure1 = new KripkeStructure(graph, agents);
        KripkeStructure structure2 = new KripkeStructure(graph, agents, true, true);

        assertNotNull(structure1);
        assertNotNull(structure2);
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.KripkeStructure#getAgents()} and
     * {@link MAKBPInterpreter.agents.KripkeStructure#getGraph()} methods.
     */
    @Test
    public void testGetters() {
        Agent agent = new Agent("a", new AgentProgram());
        Collection<Agent> agents = new HashSet<>(Arrays.asList(agent));
        KripkeWorld world = new KripkeWorld(new HashMap<>());
        Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
        map.put(agent, new HashSet<>(Arrays.asList(world)));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        graph.put(world, map);

        KripkeStructure structure = new KripkeStructure(graph, agents);

        assertEquals(agents, structure.getAgents());
        assertEquals(graph, structure.getGraph());
    }

    /**
     * Tests the {@link MAKBPInterpreter.agents.KripkeStructure#equals(Object)}
     * method.
     */
    @Test
    public void testEquals() {
        Agent agent = new Agent("a", new AgentProgram());
        Collection<Agent> agents = new HashSet<>(Arrays.asList(agent));
        Collection<Agent> agents2 = new HashSet<>(Arrays.asList(agent, new Agent("a", new AgentProgram())));
        KripkeWorld world = new KripkeWorld(new HashMap<>());
        Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
        map.put(agent, new HashSet<>(Arrays.asList(world)));
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        graph.put(world, map);
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph2 = new HashMap<>();

        KripkeStructure structure = new KripkeStructure(graph, agents);
        KripkeStructure structure2 = new KripkeStructure(new HashMap<>(graph), new HashSet<>(agents));
        KripkeStructure structure3 = new KripkeStructure(graph2, agents2);
        KripkeStructure structure4 = new KripkeStructure(graph2, agents);

        assertTrue(structure.equals(structure2));
        assertFalse(structure.equals(structure3));
        assertFalse(structure.equals(structure4));
        assertFalse(structure3.equals(structure2));
        assertFalse(structure3.equals(structure4));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeStructure#addReflexiveArcs(java.util.Map, java.util.Collection)}
     * method.
     */
    @Test
    public void testAddReflexiveArcs() {
        Agent agent = new Agent("a", new AgentProgram());
        Collection<Agent> agents = new HashSet<>(Arrays.asList(agent));
        KripkeWorld world1 = new KripkeWorld(new HashMap<>());
        KripkeWorld world2 = new KripkeWorld(new HashMap<>());

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
        map.put(agent, new HashSet<>());
        graph.put(world1, map);
        graph.put(world2, map);

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> expectedGraph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> expectedMap = new HashMap<>();
        expectedMap.put(agent, new HashSet<>(Arrays.asList(world1)));
        expectedGraph.put(world1, expectedMap);
        Map<Agent, Set<KripkeWorld>> expectedMap2 = new HashMap<>();
        expectedMap2.put(agent, new HashSet<>(Arrays.asList(world2)));
        expectedGraph.put(world2, expectedMap2);

        KripkeStructure structure = new KripkeStructure(graph, agents, true, true);

        assertEquals(expectedGraph, structure.addReflexiveArcs(graph, agents));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeStructure#addSymetricArcs(java.util.Map, java.util.Collection)}
     * method.
     */
    @Test
    public void testAddSymetricArcs() {
        Agent agent = new Agent("a", new AgentProgram());
        Collection<Agent> agents = new HashSet<>(Arrays.asList(agent));
        KripkeWorld world1 = new KripkeWorld(new HashMap<>());
        KripkeWorld world2 = new KripkeWorld(new HashMap<>());

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
        map.put(agent, new HashSet<>(Arrays.asList(world2)));
        graph.put(world1, map);
        graph.put(world2, new HashMap<>());

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> expectedGraph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> expectedMap = new HashMap<>();
        expectedMap.put(agent, new HashSet<>(Arrays.asList(world2)));
        expectedGraph.put(world1, expectedMap);
        Map<Agent, Set<KripkeWorld>> expectedMap2 = new HashMap<>();
        expectedMap2.put(agent, new HashSet<>(Arrays.asList(world1)));
        expectedGraph.put(world2, expectedMap2);

        KripkeStructure structure = new KripkeStructure(graph, agents, true, true);

        assertEquals(expectedGraph, structure.addSymetricArcs(graph, agents));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeStructure#getWorldFromOtherWorldAndAgent(MAKBPInterpreter.agents.KripkeWorld, MAKBPInterpreter.agents.Agent)}
     * method.
     */
    @Test
    public void testGetWorldFromOtherWorldAndAgent() {
        Agent agent = new Agent("a", new AgentProgram());
        Agent agent2 = new Agent("b", new AgentProgram());
        Collection<Agent> agents = new HashSet<>(Arrays.asList(agent, agent2));
        KripkeWorld world1 = new KripkeWorld(new HashMap<>());
        KripkeWorld world2 = new KripkeWorld(new HashMap<>());
        KripkeWorld world3 = new KripkeWorld(new HashMap<>());

        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
        map.put(agent, new HashSet<>(Arrays.asList(world2)));
        map.put(agent2, new HashSet<>(Arrays.asList(world3)));
        graph.put(world1, map);
        graph.put(world2, new HashMap<>());
        graph.put(world3, new HashMap<>());

        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);
        System.out.println(structure.getGraph());

        assertEquals(new HashSet<>(Arrays.asList(world1, world2)),
                structure.getWorldFromOtherWorldAndAgent(world1, agent));
        assertEquals(new HashSet<>(Arrays.asList(world1, world3)),
                structure.getWorldFromOtherWorldAndAgent(world1, agent2));
        assertEquals(new HashSet<>(Arrays.asList(world1, world2)),
                structure.getWorldFromOtherWorldAndAgent(world2, agent));
        assertEquals(new HashSet<>(Arrays.asList(world2)), structure.getWorldFromOtherWorldAndAgent(world2, agent2));
        assertEquals(new HashSet<>(Arrays.asList(world3)), structure.getWorldFromOtherWorldAndAgent(world3, agent));
        assertEquals(new HashSet<>(Arrays.asList(world1, world3)),
                structure.getWorldFromOtherWorldAndAgent(world3, agent2));
    }

    /**
     * Tests the
     * {@link MAKBPInterpreter.agents.KripkeStructure#publicAnnouncement(MAKBPInterpreter.logic.Formula)}
     * method.
     */
    @Test
    public void testPublicAnnouncement() {
        KripkeStructure structure;
        Atom atom1 = new Atom("1");
        Atom atom2 = new Atom("2");
        Agent agent = new Agent("a", new AgentProgram());
        Formula Ka = new AgentKnowledge(agent, atom1);
        Formula Ka2 = new Not(new AgentKnowledge(agent, atom1));

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

        structure = new KripkeStructure(graph, Arrays.asList(agent));
        try {
            structure.publicAnnouncement(new And(Arrays.asList(atom1, atom2)));
            assertEquals(1, structure.getGraph().size());
            assertEquals(new HashSet<>(Arrays.asList(world2)), structure.getGraph().keySet());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        structure = new KripkeStructure(graph, Arrays.asList(agent));
        try {
            structure.publicAnnouncement(new Not(atom1));
            assertEquals(0, structure.getGraph().size());
            assertEquals(new HashSet<>(), structure.getGraph().keySet());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        structure = new KripkeStructure(graph, Arrays.asList(agent));
        try {
            structure.publicAnnouncement(Ka);
            assertEquals(2, structure.getGraph().size());
            assertEquals(new HashSet<>(Arrays.asList(world1, world2)), structure.getGraph().keySet());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }

        structure = new KripkeStructure(graph, Arrays.asList(agent));
        try {
            structure.publicAnnouncement(Ka2);
            assertEquals(0, structure.getGraph().size());
            assertEquals(new HashSet<>(), structure.getGraph().keySet());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected thrown exception");
        }
    }
}
