package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import MAKBPInterpreter.agents.exceptions.KripkeStructureInvalidRuntimeException;
import MAKBPInterpreter.logic.Formula;

/**
 * Represents a system of reasoning for modal logic.
 */
public class KripkeStructure {
    /**
     * The memory and knowledge data structure.
     */
    private Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph;

    /**
     * Constructor.
     * 
     * @param graph                 graph to assigned to the structure
     * @param agents                list of agents to assigned to the structure
     * @param reflexiveArcsIncluded if {@code false}, we call the
     *                              {@link #addReflexiveArcs(Map, Collection)}
     *                              mathod
     * @param symetricArcsIncluded  if {@code false}, we call the
     *                              {@link #addSymetricArcs(Map, Collection)} method
     */
    public KripkeStructure(Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph, Collection<Agent> agents,
            boolean reflexiveArcsIncluded, boolean symetricArcsIncluded) {
        if (!symetricArcsIncluded) {
            graph = this.addSymetricArcs(graph, agents);
        }
        if (!reflexiveArcsIncluded) {
            graph = this.addReflexiveArcs(graph, agents);
        }
        this.graph = graph;
    }

    /**
     * Default constructor.
     * 
     * @implNote We call the
     *           {@link #KripkeStructure(Map, Collection, boolean, boolean)}
     *           constructor with {@code false} for the default boolean parameters.
     *           We add the reflexive and symetric arc to the graph.
     * @param graph  graph to assigned to the structure
     * @param agents list of agents to assigned to the structure
     */
    public KripkeStructure(Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph, Collection<Agent> agents) {
        this(graph, agents, false, false);
    }

    /**
     * Copy constructor.
     * 
     * @param structure structure to copy
     */
    public KripkeStructure(KripkeStructure structure) {
        this.graph = new HashMap<>();
        for (KripkeWorld key : structure.graph.keySet()) {
            Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
            for (Agent agent : structure.graph.get(key).keySet()) {
                map.put(agent, new HashSet<>(structure.graph.get(key).get(agent)));
            }
            this.graph.put(key, map);
        }
    }

    /**
     * Initializes an empty graph with all worlds and agents as keys.
     * 
     * @param worlds all worlds of the structure
     * @param agents all agents in the structure
     * @return an initialized graph
     */
    protected Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> initializeGraph(Collection<KripkeWorld> worlds,
            Collection<Agent> agents) {
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        for (KripkeWorld world : worlds) {
            Map<Agent, Set<KripkeWorld>> map = new HashMap<>();
            for (Agent agent : agents) {
                map.put(agent, new HashSet<>());
            }
            graph.put(world, map);
        }
        return graph;
    }

    /**
     * Adds reflexive arcs into the {@code graph} object.
     * 
     * A copy is realized so the {@code graph} object is not modified.
     * 
     * @param graph  current graph knowledge
     * @param agents collection of agents
     * @return the modified copy of {@code graph}
     */
    public Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> addReflexiveArcs(
            Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph,
            Collection<Agent> agents) {
        // initialization
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph2 = this.initializeGraph(graph.keySet(), agents);

        for (KripkeWorld key : graph.keySet()) {
            Map<Agent, Set<KripkeWorld>> linkGraph1 = graph.get(key);
            Map<Agent, Set<KripkeWorld>> linkGraph2 = graph2.get(key);

            // copy
            for (Agent key2 : linkGraph1.keySet()) {
                Set<KripkeWorld> value = linkGraph1.get(key2);
                linkGraph2.get(key2).addAll(value);
            }

            // add reflexive arcs
            for (Agent agent : linkGraph2.keySet()) {
                graph2.get(key).get(agent).add(key);
            }
        }
        return graph2;
    }

    /**
     * Adds symetric arcs into the {@code graph} object.
     * 
     * A copy is realized so the {@code graph} object is not modified.
     * 
     * @param graph  current graph knowledge
     * @param agents collection of agents
     * @return the modified copy of {@code graph}
     */
    public Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> addSymetricArcs(
            Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph, Collection<Agent> agents) {
        // initialization
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph2 = this.initializeGraph(graph.keySet(), agents);

        for (KripkeWorld key : graph.keySet()) {
            Map<Agent, Set<KripkeWorld>> linkGraph1 = graph.get(key);
            Map<Agent, Set<KripkeWorld>> linkGraph2 = graph2.get(key);

            // copy
            for (Agent key2 : linkGraph1.keySet()) {
                Set<KripkeWorld> value = linkGraph1.get(key2);
                linkGraph2.get(key2).addAll(value);
            }

            // add symetric arcs
            for (Agent agent : linkGraph2.keySet()) {
                for (KripkeWorld world : linkGraph2.getOrDefault(agent, new HashSet<>())) {
                    graph2.get(world).get(agent).add(key);
                }
            }
        }
        return graph2;
    }

    /**
     * Called when a public announcement is formulated.
     * 
     * @param formula formula to announce
     * @throws Exception
     */
    public void publicAnnouncement(Formula formula) throws Exception {
        // we check worlds that not satisfied the formula
        Set<KripkeWorld> worldsToRemove = new HashSet<>();
        for (KripkeWorld world : this.graph.keySet()) {
            if (!world.satisfied(formula, this)) {
                worldsToRemove.add(world);
            }
        }

        // we remove worlds from the graph
        for (KripkeWorld world : worldsToRemove) {
            this.graph.remove(world);
            for (KripkeWorld key : this.graph.keySet()) {
                for (Agent agent : this.graph.get(key).keySet()) {
                    if (this.graph.get(key).get(agent).contains(world)) {
                        this.graph.get(key).get(agent).remove(world);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String repr = "Kripke[";
        for (KripkeWorld world : this.graph.keySet()) {
            repr += world.toString() + ", ";
        }
        return repr + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof KripkeStructure))
            return false;

        KripkeStructure structure = (KripkeStructure) other;

        // different size
        if (structure.graph.size() != this.graph.size()) {
            return false;
        }

        // check each nodes and edges
        for (KripkeWorld key : this.graph.keySet()) {
            if (!structure.graph.containsKey(key)) {
                return false;
            }

            for (Agent key2 : this.graph.get(key).keySet()) {
                if (!structure.graph.get(key).containsKey(key2)) {
                    return false;
                }

                if (!structure.graph.get(key).get(key2).equals(this.graph.get(key).get(key2))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.graph);
    }

    /**
     * Gets the worlds linked to a world via an agent.
     * 
     * Can be used to determine if an agent knows his state (only one world in the
     * set which is the {@code world} passed) if the {@code world} passed is the
     * real one.
     * 
     * @param world world to test
     * @param agent agent to test
     * @return a set of worlds
     */
    public Set<KripkeWorld> getWorldsFromOtherWorldAndAgent(KripkeWorld world, Agent agent) {
        if (!this.graph.containsKey(world)) {
            throw new KripkeStructureInvalidRuntimeException(
                    "the world " + world + " no longer exists in this structure");
        }
        return this.graph.get(world).get(agent);
    }

    /**
     * Gets the graph used by the structure.
     * 
     * @return reasoning graph
     */
    public Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> getGraph() {
        return this.graph;
    }

    /**
     * Gets the collection of remaining worlds in the structure.
     * 
     * @return collection of Kripke worlds
     */
    public Collection<KripkeWorld> getWorlds() {
        return this.graph.keySet();
    }
}
