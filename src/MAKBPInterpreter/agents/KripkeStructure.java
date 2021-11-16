package MAKBPInterpreter.agents;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.exceptions.FormulaNotSupported;

public class KripkeStructure {
    private Map<KripkeWorld, Map<KripkeWorld, Agent>> graph;
    private Collection<Agent> agents;

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

    public KripkeStructure(Map<KripkeWorld, Map<KripkeWorld, Agent>> graph, Collection<Agent> agents) {
        this(graph, agents, false, false);
    }

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

    public void publicAnnouncement(Formula formula) throws FormulaNotSupported {
        Set<KripkeWorld> worldsToRemove = new HashSet<>();
        for (KripkeWorld world : this.graph.keySet()) {
            if (!world.satisfied(formula, this)) {
                worldsToRemove.add(world);
            }
        }

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
            if (structure.graph.size() != this.graph.size()) {
                return false;
            }
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
