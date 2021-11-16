package MAKBPInterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

public class MuddyChildrenProblem {
    public static void main(String[] args) {
        System.out.println("Muddy Children Problem");

        // declare actions
        Action denounceItself = new Action() {
            @Override
            public Object performs(Object... objects) throws IllegalArgumentException {
                if (objects.length == 0) {
                    throw new IllegalArgumentException("Need at least the agent object");
                }
                Agent agent = (Agent) objects[0];
                System.out.println("L'agent " + agent.getName() + " s'est dénoncé");
                return null;
            }
        };
        Action beQuiet = new Action() {
            @Override
            public Object performs(Object... objects) throws IllegalArgumentException {
                if (objects.length == 0) {
                    throw new IllegalArgumentException("Need at least the agent object");
                }
                Agent agent = (Agent) objects[0];
                System.out.println("L'agent " + agent.getName() + " s'est tait");
                return null;
            }
        };

        // create agents
        List<Agent> agents = new ArrayList<>();
        for (Integer i = 0; i < 4; i++) {
            SortedMap<Formula, Action> conditions = new TreeMap<>();
            Agent agent = new Agent(i.toString(), conditions);
            conditions.put(new AgentKnowledge(agent, new Atom("sale")), denounceItself);
            conditions.put(new Not(new AgentKnowledge(agent, new Atom("sale"))), beQuiet);
        }

        // create graph
        Map<KripkeWorld, Map<KripkeWorld, Agent>> graph = new HashMap<>();

    }
}
