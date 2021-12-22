package MAKBPInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;

public class MuddyChildrenProblem {
    public static void main(String[] args) {
        System.out.println("Muddy Children Problem (n=3, k=2)");

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

        // create agents program
        List<Agent> agents = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (Integer i = 1; i < 4; i++) {
            AgentProgram program = new AgentProgram();
            Agent agent = new Agent(i.toString(), program);
            Atom atom = new Atom(i.toString() + "_propre");
            atoms.add(atom);
            program.put(new AgentKnowledge(agent, new Not(atom)), denounceItself);
            program.put(null, beQuiet);
            agents.add(agent);
        }

        // create graph
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        KripkeWorld realWorld;
        {
            // all are clean
            Map<Atom, Boolean> assignmentWorld1 = new HashMap<>();
            assignmentWorld1.put(atoms.get(0), true);
            assignmentWorld1.put(atoms.get(1), true);
            assignmentWorld1.put(atoms.get(2), true);
            KripkeWorld world1 = new KripkeWorld(Integer.toString(1), assignmentWorld1);

            // 2 & 3 are clean
            Map<Atom, Boolean> assignmentWorld2 = new HashMap<>();
            assignmentWorld2.put(atoms.get(0), false);
            assignmentWorld2.put(atoms.get(1), true);
            assignmentWorld2.put(atoms.get(2), true);
            KripkeWorld world2 = new KripkeWorld(Integer.toString(2), assignmentWorld2);

            // 3 is clean
            Map<Atom, Boolean> assignmentWorld3 = new HashMap<>();
            assignmentWorld3.put(atoms.get(0), false);
            assignmentWorld3.put(atoms.get(1), false);
            assignmentWorld3.put(atoms.get(2), true);
            KripkeWorld world3 = new KripkeWorld(Integer.toString(3), assignmentWorld3);

            // 1 & 3 are clean
            Map<Atom, Boolean> assignmentWorld4 = new HashMap<>();
            assignmentWorld4.put(atoms.get(0), true);
            assignmentWorld4.put(atoms.get(1), false);
            assignmentWorld4.put(atoms.get(2), true);
            KripkeWorld world4 = new KripkeWorld(Integer.toString(4), assignmentWorld4);

            // 1 & 2 are clean
            Map<Atom, Boolean> assignmentWorld5 = new HashMap<>();
            assignmentWorld5.put(atoms.get(0), true);
            assignmentWorld5.put(atoms.get(1), true);
            assignmentWorld5.put(atoms.get(2), false);
            KripkeWorld world5 = new KripkeWorld(Integer.toString(5), assignmentWorld5);

            // 1 is clean
            Map<Atom, Boolean> assignmentWorld6 = new HashMap<>();
            assignmentWorld6.put(atoms.get(0), true);
            assignmentWorld6.put(atoms.get(1), false);
            assignmentWorld6.put(atoms.get(2), false);
            KripkeWorld world6 = new KripkeWorld(Integer.toString(6), assignmentWorld6);

            // 2 is clean
            Map<Atom, Boolean> assignmentWorld7 = new HashMap<>();
            assignmentWorld7.put(atoms.get(0), false);
            assignmentWorld7.put(atoms.get(1), true);
            assignmentWorld7.put(atoms.get(2), false);
            KripkeWorld world7 = new KripkeWorld(Integer.toString(7), assignmentWorld7);

            // none are clean
            Map<Atom, Boolean> assignmentWorld8 = new HashMap<>();
            assignmentWorld8.put(atoms.get(0), false);
            assignmentWorld8.put(atoms.get(1), false);
            assignmentWorld8.put(atoms.get(2), false);
            KripkeWorld world8 = new KripkeWorld(Integer.toString(8), assignmentWorld8);

            realWorld = world3;

            {
                // World 1
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world5)));
                graph.put(world1, links);
            }
            {
                // World 2
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world7)));
                graph.put(world2, links);
            }
            {
                // World 3
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world8)));
                graph.put(world3, links);
            }
            {
                // World 4
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world6)));
                graph.put(world4, links);
            }
            {
                // World 5
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world7)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world6)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                graph.put(world5, links);
            }
            {
                // World 6
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world8)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world5)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                graph.put(world6, links);
            }
            {
                // World 7
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world5)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world8)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                graph.put(world7, links);
            }
            {
                // World 8
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world6)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world7)));
                links.put(agents.get(2), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                graph.put(world8, links);
            }
        }

        KripkeStructure structure = new KripkeStructure(graph, agents, false, true);
        System.out.println("------------------= Before any call ------------------=");
        System.out.println(structure);
        System.out.println(structure.getGraph());
        System.out.println("----------------- End Before any call -----------------");
        Formula motherFormula = new Or(new Not(atoms.get(0)), new Not(atoms.get(1)), new Not(atoms.get(2)));
        Formula knowledgeFormula = new And(
                new Not(new AgentKnowledge(agents.get(0), new Not(atoms.get(0)))),
                new Not(new AgentKnowledge(agents.get(1), new Not(atoms.get(1)))),
                new Not(new AgentKnowledge(agents.get(2), new Not(atoms.get(2)))));
        // TODO: regarder observation de chaque agent (ajouter une méthode car pas une
        // annonce publique)
        try {
            System.out.println("======================== k = 1 ========================");
            structure.publicAnnouncement(motherFormula);
            System.out.println("------------------ After first call -------------------");
            System.out.println(structure);
            System.out.println("---------------- End After first call -----------------\n");
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println("------------- After first deduction call --------------");
            System.out.println(structure);
            System.out.println("----------- End After first deduction call ------------\n");

            System.out.println(structure.getGraph());
            System.out.println("======================== k = 2 ========================");
            structure.publicAnnouncement(motherFormula);
            System.out.println("------------------ After second call ------------------");
            System.out.println(structure);
            System.out.println("---------------- End After second call ----------------\n");
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println("------------- After second deduction call -------------");
            System.out.println(structure);
            System.out.println("----------- End After second deduction call -----------\n");
            // TODO: to fix because we have the wrong worlds
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
