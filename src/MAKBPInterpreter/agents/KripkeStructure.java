package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.exceptions.FormulaNotSupported;

/**
 * Represents a system of raisonning for modal logic.
 */
public class KripkeStructure {
    /**
     * The memory and knowledge data structure.
     */
    private Map<KripkeWorld, Map<KripkeWorld, Agent>> graph;

    /**
     * Collection of agents representing all the current agents in the environment.
     */
    private Collection<Agent> agents;

    /**
     * Constructor.
     * 
     * @param graph                 graph to assigned to the structure
     * @param agents                list of agents to assigned to the structure
     * @param reflexiveArcsIncluded if {@code false}, we call the
     *                              {@link #addReflexiveArcs(Map, Collection)}
     *                              mathod
     * @param symetricArcsIncluded  if {@code false}, we call the
     *                              {@link #addSymetricArcs(Map)} method
     */
    public KripkeStructure(Map<KripkeWorld, Map<KripkeWorld, Agent>> graph, Collection<Agent> agents,
            boolean reflexiveArcsIncluded, boolean symetricArcsIncluded) {
        this.agents = agents;
        if (!symetricArcsIncluded) {
            graph = this.addSymetricArcs(graph);
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
    public KripkeStructure(Map<KripkeWorld, Map<KripkeWorld, Agent>> graph, Collection<Agent> agents) {
        this(graph, agents, false, false);
    }

    /**
     * Adds reflexive arcs into the {@code graph} object.
     * 
     * A copy is realized so the {@code graph} object is not modified.
     * 
     * @param graph current graph knowledge
     * @return the modified copy of {@code graph}
     */
    public Map<KripkeWorld, Map<KripkeWorld, Agent>> addReflexiveArcs(Map<KripkeWorld, Map<KripkeWorld, Agent>> graph,
            Collection<Agent> agents) {
        Map<KripkeWorld, Map<KripkeWorld, Agent>> graph2 = new HashMap<>();
        for (KripkeWorld key : graph.keySet()) {
            Map<KripkeWorld, Agent> linkGraph1 = graph.get(key);
            Map<KripkeWorld, Agent> linkGraph2 = graph2.get(key);

            // copy
            for (KripkeWorld key2 : linkGraph1.keySet()) {
                Agent value = linkGraph1.get(key2);
                linkGraph2.put(key2, value);
            }

            // add reflexive arcs
            for (Agent agent : agents) {
                linkGraph2.put(key, agent);
            }
        }
        return graph2;
    }

    /**
     * Adds symetric arcs into the {@code graph} object.
     * 
     * A copy is realized so the {@code graph} object is not modified.
     * 
     * @param graph current graph knowledge
     * @return the modified copy of {@code graph}
     */
    public Map<KripkeWorld, Map<KripkeWorld, Agent>> addSymetricArcs(Map<KripkeWorld, Map<KripkeWorld, Agent>> graph) {
        Map<KripkeWorld, Map<KripkeWorld, Agent>> graph2 = new HashMap<>();
        for (KripkeWorld key : graph.keySet()) {
            if (!graph2.containsKey(key)) {
                graph2.put(key, new HashMap<>());
            }
            Map<KripkeWorld, Agent> linkGraph1 = graph.get(key);
            Map<KripkeWorld, Agent> linkGraph2 = graph2.get(key);

            for (KripkeWorld key2 : linkGraph1.keySet()) {
                Agent value = linkGraph1.get(key2); // link where in graph
                linkGraph2.put(key2, value);
                if (!graph2.containsKey(key)) {
                    graph2.put(key2, new HashMap<>());
                }
                graph2.get(key2).put(key, value); // add symetric
            }
        }
        return graph2;
    }

    /**
     * Called when a public announcement is formulated.
     * 
     * @param formula formula to announce
     * @throws FormulaNotSupported if a formula is not supported by the program
     */
    public void publicAnnouncement(Formula formula) throws FormulaNotSupported {
        // we check the world that not satisfied the formula
        Set<KripkeWorld> worldsToRemove = new HashSet<>();
        for (KripkeWorld world : this.graph.keySet()) {
            if (!world.satisfied(formula)) {
                worldsToRemove.add(world);
            }
        }

        // we remove the worlds in the graph
        for (KripkeWorld world : worldsToRemove) {
            graph.remove(world);
            for (KripkeWorld key : this.graph.keySet()) {
                if (this.graph.get(key).containsKey(world)) {
                    this.graph.get(key).remove(world);
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
        if (other instanceof KripkeStructure) {
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
                for (KripkeWorld key2 : this.graph.get(key).keySet()) {
                    if (!structure.graph.get(key).containsKey(key2)) {
                        return false;
                    }
                    if (structure.graph.get(key).get(key2) != this.graph.get(key).get(key2)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
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
    public Set<KripkeWorld> getWorldFromOtherWorldAndAgent(KripkeWorld world, Agent agent) {
        Set<KripkeWorld> set = new HashSet<>();
        for (Map.Entry<KripkeWorld, Agent> entry : this.graph.getOrDefault(world, new HashMap<>()).entrySet()) {
            if (entry.getValue().equals(agent)) {
                set.add(entry.getKey());
            }
        }
        return set;
    }
}
