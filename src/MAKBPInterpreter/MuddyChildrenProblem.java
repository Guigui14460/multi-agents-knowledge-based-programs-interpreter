package MAKBPInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.AgentProgram;
import MAKBPInterpreter.agents.Diamond;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.interpreter.MAKBPInterpreter;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;
import MAKBPInterpreter.logic.Or;

/**
 * Class representing the problem of muddy children.
 * 
 * The problem is as follows:
 * <ul>
 * <li>the children go to play but the father has warned them not to get
 * dirty;</li>
 * <li>they come back and if one of them is dirty, the father says "at least
 * one of you is dirty".</li>
 * </ul>
 * 
 * The goal is this: to make sure that only the children who know they are dirty
 * have to tell on each other. To do this, the children know the condition of
 * the other children's foreheads, but a considered child does not know his
 * condition. Therefore, he has to reason about the actions and programs of the
 * other children in order to know if he himself is dirty or not.
 */
public class MuddyChildrenProblem {
    /**
     * Pretty prints a Kripke structure.
     * 
     * @param structure              structure to pretty print
     * @param atomPredicateExtractor function to extract the predicate
     * @return worlds of the structure in prettyfied string
     */
    public static String prettyPrintKripkeStructure(KripkeStructure structure,
            Function<String, String> atomPredicateExtractor) {
        String string = "[";

        Collection<KripkeWorld> worlds = structure.getWorlds();
        for (KripkeWorld world : worlds) {
            string += "{";
            for (Map.Entry<Atom, Boolean> entry : world.getAssignment().entrySet()) {
                string += entry.getValue() ? "" : "~";
                string += atomPredicateExtractor.apply(entry.getKey().toString());
                string += " ";
            }
            string += "}, ";
        }
        return string + "]";
    }

    /**
     * Generalized problem using the multi-agent knowledge-based program (MABKP)
     * interpreter.
     * To encode the real world, we pass a decimal number which
     * will be then decoded in a binary number allowing to
     * create the worlds in an automatic way.
     * 
     * For example, if n = 4, realWorld will be between 0 and 15
     * included. If we choose realWorld = 13 ( = (1101)_2 ), the real world
     * will be the world where :
     * <ul>
     * <li>agents 0, 2, 3 are dirty</li>
     * <li>agent 1 is clean</li>
     * </ul>
     * 
     * @param n            number of children
     * @param realWorld    real world encoded in decimal
     * @param maxIteration maximum number of iteration to avoid an infinite while
     *                     loop
     * 
     * @throws IllegalArgumentException thrown the real world can't exists
     */
    public static void problem(int n, int realWorld, int maxIteration) {
        System.out.println("'A' equivalent to 'Agent A is muddy'");
        if (realWorld < 0 || realWorld > Math.pow(2, n)) {
            throw new IllegalArgumentException(
                    "The chosen real world can't exists! Please choose one between 0 and 2^" + Integer.toString(n));
        }

        // agent creation
        List<Agent> agents = new ArrayList<>();
        Set<Agent> muddyAgents = new HashSet<>();
        List<AgentProgram> agentPrograms = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            agentPrograms.add(new AgentProgram());
            agents.add(new Agent(Integer.toString(i), agentPrograms.get(i)));
            atoms.add(new Atom(Integer.toString(i) + " is muddy"));
        }

        // programs creation
        Map<Action, List<Object>> objects = new HashMap<>();
        Map<Agent, Set<Atom>> atomsAssociation = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Atom atom = atoms.get(i);
            Agent agent = agents.get(i);
            AgentProgram ap = agentPrograms.get(i);

            // action: denounceItSelf
            Action denounceItself = new Action() {
                @Override
                public Object performs(Object... objects) throws IllegalArgumentException {
                    if (objects.length < 1) {
                        throw new IllegalArgumentException("Need at least the agent object");
                    }
                    Agent agent = (Agent) objects[0];
                    System.out.println("The agent " + agent.getName() + " reported himself");
                    return agent;
                }
            };
            ap.put(new AgentKnowledge(agent, atom), denounceItself);
            objects.put(denounceItself, new ArrayList<>(Arrays.asList(agent)));

            // action: beQuiet
            Action beQuiet = new Action() {
                @Override
                public Object performs(Object... objects) throws IllegalArgumentException {
                    if (objects.length < 1) {
                        throw new IllegalArgumentException("Need at least the agent object");
                    }
                    Agent agent = (Agent) objects[0];
                    System.out.println("The agent " + agent.getName() + " is silent");
                    return null;
                }
            };
            ap.put(null, beQuiet);
            objects.put(beQuiet, new ArrayList<>(Arrays.asList(agent)));

            atomsAssociation.put(agent, new HashSet<>(Arrays.asList(atom)));
        }

        // permissions creation
        Map<Agent, Set<Agent>> permissions = new HashMap<>();
        for (Agent a : agents) {
            Set<Agent> set = new HashSet<>(agents);
            set.remove(a);
            permissions.put(a, set);
        }

        // kripke worlds and structure
        boolean realWorldIsRandom = false;
        List<KripkeWorld> worlds = new ArrayList<>();
        KripkeWorld realWorldObject = null;
        for (int i = 0; i < Math.pow(2, n); i++) {
            Map<Atom, Boolean> assignment = new HashMap<>();
            for (int agentAtom = 0; agentAtom < n; agentAtom++) {
                // for the stop condition
                if (i == realWorld && ((i / ((int) Math.pow(2, agentAtom))) % 2) != 0) {
                    muddyAgents.add(agents.get(agentAtom));
                }

                // for the world assignment
                assignment.put(atoms.get(agentAtom), ((i / ((int) Math.pow(2, agentAtom))) % 2) != 0);
            }
            KripkeWorld world = new KripkeWorld(assignment);
            worlds.add(world);
            if (i == realWorld) {
                realWorldObject = world;
            }
        }
        if (realWorldObject == null) {
            realWorldIsRandom = true;
            realWorldObject = worlds.get((new Random()).nextInt(worlds.size()));
        }
        Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = new HashMap<>();
        for (KripkeWorld world : worlds) {
            graph.put(world, new HashMap<>());
            for (int i = 0; i < agents.size(); i++) {
                Agent agent = agents.get(i);
                graph.get(world).put(agent, new HashSet<>());
                for (KripkeWorld world2 : worlds) {
                    Collection<Atom> differences = world.differencesBetweenWorlds(world2);
                    if (differences.size() > 1 || differences.size() == 0) {
                        continue;
                    }

                    if (atomsAssociation.get(agent).contains(differences.toArray()[0])) {
                        graph.get(world).get(agent).add(world2);
                    }
                }
            }
        }
        KripkeStructure structure = new KripkeStructure(graph, agents, false, false);

        // father formula
        Set<Formula> operands = new HashSet<>();
        for (int i = 0; i < n; i++) {
            operands.add(atoms.get(i));
        }
        Formula fatherFormula = new Or(operands);

        Function<String, String> extractor = (predicate) -> {
            String agentNumberString = predicate.split(" ")[0];
            int agentNumber = Integer.parseInt(agentNumberString);
            int Acharcode = 'A';
            char agentChar = (char) (Acharcode + agentNumber);
            return String.valueOf(agentChar);
        };

        // interpreter
        MAKBPInterpreter interpreter = new MAKBPInterpreter(new HashSet<>(agents), structure, permissions, objects);
        System.out.println(
                "Initial structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));
        System.out.println("Real world : " + realWorld);
        System.out.println("Muddy agents : " + muddyAgents);
        try {
            // check if all kripkeworld contains only
            BiFunction<Boolean, KripkeWorld, Boolean> isFinished = (isRandom, rWObject) -> {
                if (isRandom) {
                    return interpreter.isFinished();
                }
                return interpreter.isFinished(rWObject);
            };

            BiFunction<Map<Agent, Object>, Set<Agent>, Boolean> allMuddyChildrenDenouncedThemselves = (returns,
                    muddyAgentsSet) -> {
                if (returns.size() == 0) {
                    return muddyAgentsSet.size() == 0;
                }
                for (Agent agent : muddyAgentsSet) {
                    if (!returns.containsKey(agent) || returns.get(agent) == null) {
                        return false;
                    }
                }
                return true;
            };

            int k = 0;
            Map<Agent, Object> returns = new HashMap<>();
            Map<Agent, Formula> deductions = new HashMap<>();
            while (!allMuddyChildrenDenouncedThemselves.apply(returns, muddyAgents)
                    && !isFinished.apply(realWorldIsRandom, realWorldObject) && (k < maxIteration)) {
                k++;
                System.out.println("\n====================== k = " + Integer.toString(k) + " ======================");

                // removing worlds from father and deducted formulas
                System.out.println("The father announce : 'at least one of you is muddy'");
                interpreter.publicAnnouncement(agents, fatherFormula);

                System.out.println("Children interpret deductions ...");
                interpreter.publicAnnouncement(deductions);

                System.out.println();

                // actions execution
                System.out.println("Actions execution :");
                Map<Agent, Action> actions = interpreter.getAssociatedAction(agents, realWorldObject);
                returns = interpreter.executeAction(actions);

                System.out.println();

                // reasoning system
                System.out.println("The children reflect ...");
                deductions = interpreter.reverseEngineering(actions);
                deductions = interpreter.reasoning(agents, deductions);

                Thread.sleep(500);
            }

            System.out.println("\n\n===============  Execution stopped ===============");
            if (allMuddyChildrenDenouncedThemselves.apply(returns, muddyAgents)) {
                System.out.println("==> Found in k = " + k);
            } else {
                System.out.println("==> Stopped at iteration " + k + " (not found)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    final static Action denounceItself = new Action() {
        @Override
        public Object performs(Object... objects) throws IllegalArgumentException {
            if (objects.length == 0) {
                throw new IllegalArgumentException("Need at least the agent object");
            }
            Agent agent = (Agent) objects[0];
            System.out.println("The agent " + agent.getName() + " reported himself");
            return null;
        }
    };
    final static Action beQuiet = new Action() {
        @Override
        public Object performs(Object... objects) throws IllegalArgumentException {
            if (objects.length == 0) {
                throw new IllegalArgumentException("Need at least the agent object");
            }
            Agent agent = (Agent) objects[0];
            System.out.println("The agent " + agent.getName() + " is silent");
            return null;
        }
    };

    /**
     * Problem with two children and one child among them is dirty (without the
     * MAKBP interpreter).
     */
    public static void problemN2K1() {
        System.out.println("Muddy Children Problem (n=2, k=1) : only the agent B is clean");
        System.out.println("==> Point of view of agent A");
        System.out.println("'A' equivalent to 'Agent A is clean'");
        System.out.println("Agent A = Agent 0 ; Agent B = Agent 1");

        // create agents program
        List<Agent> agents = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (Integer i = 0; i < 2; i++) {
            AgentProgram program = new AgentProgram();
            Agent agent = new Agent(i.toString(), program);
            Atom atom = new Atom(i.toString() + " is clean");
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
            KripkeWorld world1 = new KripkeWorld(Integer.toString(1), assignmentWorld1);

            // 2 is clean
            Map<Atom, Boolean> assignmentWorld2 = new HashMap<>();
            assignmentWorld2.put(atoms.get(0), false);
            assignmentWorld2.put(atoms.get(1), true);
            KripkeWorld world2 = new KripkeWorld(Integer.toString(2), assignmentWorld2);

            // all are muddy
            Map<Atom, Boolean> assignmentWorld3 = new HashMap<>();
            assignmentWorld3.put(atoms.get(0), false);
            assignmentWorld3.put(atoms.get(1), false);
            KripkeWorld world3 = new KripkeWorld(Integer.toString(3), assignmentWorld3);

            // 1 is clean
            Map<Atom, Boolean> assignmentWorld4 = new HashMap<>();
            assignmentWorld4.put(atoms.get(0), true);
            assignmentWorld4.put(atoms.get(1), false);
            KripkeWorld world4 = new KripkeWorld(Integer.toString(4), assignmentWorld4);

            realWorld = world2;

            {
                // World 1
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                graph.put(world1, links);
            }
            {
                // World 2
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                graph.put(world2, links);
            }
            {
                // World 3
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                graph.put(world3, links);
            }
            {
                // World 4
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                graph.put(world4, links);
            }
        }

        Function<String, String> extractor = (predicate) -> {
            String agentNumberString = predicate.split(" ")[0];
            int agentNumber = Integer.parseInt(agentNumberString);
            int Acharcode = 'A';
            char agentChar = (char) (Acharcode + agentNumber);
            return String.valueOf(agentChar);
        };

        KripkeStructure structure = new KripkeStructure(graph, agents, false, true);
        System.out.println(
                "Initial structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));
        Formula fatherFormula = new Or(new Not(atoms.get(0)), new Not(atoms.get(1)));

        try {
            System.out.println("\n\n======================== k = 1 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            // to remove impossible worlds
            structure.publicAnnouncement(new And(new Not(atoms.get(0)), atoms.get(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nReal world ? (" + realWorld + ") : "
                + new HashSet<>(Arrays.asList(realWorld)).equals(structure.getWorlds()));
    }

    /**
     * Problem with two children and both are dirty (without the MAKBP interpreter).
     */
    public static void problemN2K2() {
        System.out.println("Muddy Children Problem (n=2, k=2) : all agents are muddy");
        System.out.println("'A' equivalent to 'Agent A is clean'");
        System.out.println("Agent A = Agent 0 ; Agent B = Agent 1");

        // create agents program
        List<Agent> agents = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (Integer i = 1; i < 3; i++) {
            AgentProgram program = new AgentProgram();
            Agent agent = new Agent(i.toString(), program);
            Atom atom = new Atom(i.toString() + " is clean");
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
            KripkeWorld world1 = new KripkeWorld(Integer.toString(1), assignmentWorld1);

            // 2 is clean
            Map<Atom, Boolean> assignmentWorld2 = new HashMap<>();
            assignmentWorld2.put(atoms.get(0), false);
            assignmentWorld2.put(atoms.get(1), true);
            KripkeWorld world2 = new KripkeWorld(Integer.toString(2), assignmentWorld2);

            // all are muddy
            Map<Atom, Boolean> assignmentWorld3 = new HashMap<>();
            assignmentWorld3.put(atoms.get(0), false);
            assignmentWorld3.put(atoms.get(1), false);
            KripkeWorld world3 = new KripkeWorld(Integer.toString(3), assignmentWorld3);

            // 1 is clean
            Map<Atom, Boolean> assignmentWorld4 = new HashMap<>();
            assignmentWorld4.put(atoms.get(0), true);
            assignmentWorld4.put(atoms.get(1), false);
            KripkeWorld world4 = new KripkeWorld(Integer.toString(4), assignmentWorld4);

            realWorld = world3;

            {
                // World 1
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                graph.put(world1, links);
            }
            {
                // World 2
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                graph.put(world2, links);
            }
            {
                // World 3
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world4)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world2)));
                graph.put(world3, links);
            }
            {
                // World 4
                Map<Agent, Set<KripkeWorld>> links = new HashMap<>();
                links.put(agents.get(0), new HashSet<KripkeWorld>(Arrays.asList(world3)));
                links.put(agents.get(1), new HashSet<KripkeWorld>(Arrays.asList(world1)));
                graph.put(world4, links);
            }
        }

        Function<String, String> extractor = (predicate) -> {
            String agentNumberString = predicate.split(" ")[0];
            int agentNumber = Integer.parseInt(agentNumberString);
            int Acharcode = 'A';
            char agentChar = (char) (Acharcode + agentNumber);
            return String.valueOf(agentChar);
        };

        KripkeStructure structure = new KripkeStructure(graph, agents, false, true);
        System.out.println(
                "Initial structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));
        Formula fatherFormula = new Or(new Not(atoms.get(0)), new Not(atoms.get(1)));
        Formula knowledgeFormula;

        try {
            System.out.println("\n\n======================== k = 1 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            System.out.println("\n\n======================== k = 2 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents reflect ...");
            // a knows b is muddy and b not denounced so a knows he is muddy
            // b knows a is muddy and a not denounced so b knows he is muddy
            knowledgeFormula = new And(
                    new AgentKnowledge(agents.get(0),
                            new Not(new Or(new AgentKnowledge(agents.get(0), new Not(atoms.get(0))),
                                    new AgentKnowledge(agents.get(0), atoms.get(0))))),
                    new AgentKnowledge(agents.get(1),
                            new Not(new Or(new AgentKnowledge(agents.get(1), new Not(atoms.get(1))),
                                    new AgentKnowledge(agents.get(1), atoms.get(1))))));
            System.out.println("Annoucing deducted formula (global knwoledge formula) : " + knowledgeFormula);
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            // to remove impossible worlds
            structure.publicAnnouncement(new And(new Not(atoms.get(0)), new Not(atoms.get(1))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nReal world ? (" + realWorld + ") : "
                + new HashSet<>(Arrays.asList(realWorld)).equals(structure.getWorlds()));
    }

    /**
     * Problem with three children and thow child among them are dirty (without the
     * MAKBP interpreter).
     */
    public static void problemN3K2() {
        System.out.println("Muddy Children Problem (n=3, k=2) : only agents A and B are muddy");
        System.out.println("'A' equivalent to 'Agent A is clean'");
        System.out.println("Agent A = Agent 0 ; Agent B = Agent 1 ; Agent C = Agent 2");

        // create agents program
        List<Agent> agents = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (Integer i = 0; i < 3; i++) {
            AgentProgram program = new AgentProgram();
            Agent agent = new Agent(i.toString(), program);
            Atom atom = new Atom(i.toString() + " is clean");
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

        Function<String, String> extractor = (predicate) -> {
            String agentNumberString = predicate.split(" ")[0];
            int agentNumber = Integer.parseInt(agentNumberString);
            int Acharcode = 'A';
            char agentChar = (char) (Acharcode + agentNumber);
            return String.valueOf(agentChar);
        };

        KripkeStructure structure = new KripkeStructure(graph, agents, false, true);
        System.out.println(
                "Initial structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));
        Formula fatherFormula = new Or(new Not(atoms.get(0)), new Not(atoms.get(1)), new Not(atoms.get(2)));
        Formula knowledgeFormula;

        try {
            System.out.println("\n\n======================== k = 1 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            System.out.println("\n\n======================== k = 2 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents reflect ...");
            knowledgeFormula = new And(
                    new Diamond(agents.get(0),
                            new And(new Not(atoms.get(0)), new Not(atoms.get(1)), atoms.get(2))),
                    new Diamond(agents.get(1),
                            new And(new Not(atoms.get(0)), new Not(atoms.get(1)), atoms.get(2))),
                    new Diamond(agents.get(2),
                            new And(new Not(atoms.get(0)), new Not(atoms.get(1)), new Not(atoms.get(2)))));
            System.out.println("Annoucing deducted formula (global knwoledge formula) : " + knowledgeFormula);
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }
            // c knows is clean after a and b demounciation

            // to remove impossible worlds
            structure.publicAnnouncement(new And(new Not(atoms.get(0)), new Not(atoms.get(1)), atoms.get(2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nReal world ? (" + realWorld + ") : "
                + new HashSet<>(Arrays.asList(realWorld)).equals(structure.getWorlds()));
    }

    /**
     * Problem with three children and are all dirty (without the MAKBP
     * interpreter).
     */
    public static void problemN3K3() {
        System.out.println("Muddy Children Problem (n=3, k=3) : all agents are muddy");
        System.out.println("'A' equivalent to 'Agent A is clean'");
        System.out.println("Agent A = Agent 0 ; Agent B = Agent 1 ; Agent C = Agent 2");

        // create agents program
        List<Agent> agents = new ArrayList<>();
        List<Atom> atoms = new ArrayList<>();
        for (Integer i = 1; i < 4; i++) {
            AgentProgram program = new AgentProgram();
            Agent agent = new Agent(i.toString(), program);
            Atom atom = new Atom(i.toString() + " is clean");
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

            realWorld = world8;

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

        Function<String, String> extractor = (predicate) -> {
            String agentNumberString = predicate.split(" ")[0];
            int agentNumber = Integer.parseInt(agentNumberString);
            int Acharcode = 'A';
            char agentChar = (char) (Acharcode + agentNumber);
            return String.valueOf(agentChar);
        };

        KripkeStructure structure = new KripkeStructure(graph, agents, false, true);
        System.out.println(
                "Initial structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));
        Formula fatherFormula = new Or(new Not(atoms.get(0)), new Not(atoms.get(1)), new Not(atoms.get(2)));
        Formula knowledgeFormula;

        try {
            System.out.println("\n\n======================== k = 1 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            System.out.println("\n\n======================== k = 2 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents reflect ...");
            // a knows b and c are muddy and at least 1 muddy child and no one denounciate
            // himself
            // b knows a and b are muddy and at least 1 muddy child and no one denounciate
            // himself
            // c knows b and a are muddy and at least 1 muddy child and no one denounciate
            // himself
            knowledgeFormula = new And(
                    new AgentKnowledge(agents.get(0),
                            new Not(new Or(new AgentKnowledge(agents.get(0), new Not(atoms.get(0))),
                                    new AgentKnowledge(agents.get(0), atoms.get(0))))),
                    new AgentKnowledge(agents.get(1),
                            new Not(new Or(new AgentKnowledge(agents.get(1), new Not(atoms.get(1))),
                                    new AgentKnowledge(agents.get(1), atoms.get(1))))),
                    new AgentKnowledge(agents.get(2),
                            new Not(new Or(new AgentKnowledge(agents.get(2), new Not(atoms.get(2))),
                                    new AgentKnowledge(agents.get(2), atoms.get(2))))));
            System.out.println("Annoucing deducted formula (global knwoledge formula) : " + knowledgeFormula);
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }

            System.out.println("\n\n======================== k = 3 ========================");
            System.out.println("The father announce : 'at least one of you is muddy'");
            structure.publicAnnouncement(fatherFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents reflect ...");
            // a knows b and c are muddy and at least 2 muddy child and no one denounciate
            // himself
            // b knows a and b are muddy and at least 2 muddy child and no one denounciate
            // himself
            // c knows b and a are muddy and at least 2 muddy child and no one denounciate
            // himself
            knowledgeFormula = new And(
                    new AgentKnowledge(agents.get(0),
                            new Not(new Or(new AgentKnowledge(agents.get(0), new Not(atoms.get(0))),
                                    new AgentKnowledge(agents.get(0), atoms.get(0))))),
                    new AgentKnowledge(agents.get(1),
                            new Not(new Or(new AgentKnowledge(agents.get(1), new Not(atoms.get(1))),
                                    new AgentKnowledge(agents.get(1), atoms.get(1))))),
                    new AgentKnowledge(agents.get(2),
                            new Not(new Or(new AgentKnowledge(agents.get(2), new Not(atoms.get(2))),
                                    new AgentKnowledge(agents.get(2), atoms.get(2))))));
            System.out.println("Annoucing deducted formula (global knwoledge formula) : " + knowledgeFormula);
            structure.publicAnnouncement(knowledgeFormula);
            System.out.println(
                    "==> Structure : " + MuddyChildrenProblem.prettyPrintKripkeStructure(structure, extractor));

            System.out.println("\nAgents executes program ...");
            for (Agent agent : agents) {
                agent.performsAssociatedAction(structure, realWorld, agent);
            }
            // a, b, c knows are all muddy and all denounciate himself

            // to remove impossible worlds
            structure.publicAnnouncement(new And(new Not(atoms.get(0)), new Not(atoms.get(1)), new Not(atoms.get(2))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nReal world ? (" + realWorld + ") : "
                + new HashSet<>(Arrays.asList(realWorld)).equals(structure.getWorlds()));
    }
}
