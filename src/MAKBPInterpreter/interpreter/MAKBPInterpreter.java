package MAKBPInterpreter.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.AgentKnowledge;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.agents.exceptions.KripkeStructureInvalidRuntimeException;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;
import MAKBPInterpreter.logic.Not;

/**
 * Represents a Multi-Agent Knowledge-Based Program interpreter.
 * 
 * We can pass a default structure, collections of agents and objects,
 * and an association of permissions to initialize this.
 * 
 * We can iterate through the publicAnnoucement method to run the
 * deduction system. We can check if all agents know that are all
 * in the real world.
 */
public class MAKBPInterpreter {
    /**
     * Each agent has its own structure.
     */
    private Map<Agent, KripkeStructure> structures;

    /**
     * Each agent knows a finite set of other agents program.
     */
    private Map<Agent, Set<Agent>> permissions;

    /**
     * List of objects to pass for each available actions on the whole program.
     */
    private Map<Action, Set<Object>> objects;

    /**
     * Each agent has a set of associated atoms.
     */
    private Map<Agent, Set<Atom>> atomsAssociation;

    /**
     * Constructor.
     * 
     * @param agents           set of agents
     * @param structure        default structure to copy for each agents
     * @param permissions      agent programs knowledge for each agent
     * @param objects          sets of objects to pass for action execution
     * @param atomsAssociation atoms associated to an agent
     */
    public MAKBPInterpreter(Set<Agent> agents, KripkeStructure structure,
            Map<Agent, Set<Agent>> permissions, Map<Action, Set<Object>> objects,
            Map<Agent, Set<Atom>> atomsAssociation) {
        this.structures = new HashMap<>();
        for (Agent agent : agents) {
            KripkeStructure newStructure = new KripkeStructure(structure);
            this.structures.put(agent, newStructure);
        }
        this.permissions = permissions;
        this.objects = objects;
        this.atomsAssociation = atomsAssociation;
    }

    /**
     * Annoucement of a public formula.
     * 
     * All of the deduction system run in this method. For each agent :
     * <ul>
     * <li>we annouce the formula in its structure</li>
     * <li>retrieve knowledge on a pointed world</li>
     * <li>get an action through agent program</li>
     * <li>we make a reverse engineering on the action to retrieve agent knowledge
     * in function of the agent program</li>
     * </ul>
     * In function of permissions, we distribute agent knowledge of one of this to
     * others.
     * 
     * We return the return value of each actions.
     * 
     * @param formula      formula to announce
     * @param pointedWorld pointed world to analyse
     * @return return values of each executed actions
     * @throws Exception            thrown when thrown in structure public
     *                              annoucement method
     * @throws NullPointerException thrown when the {@code pointedWorld} is no more
     *                              in at least one of the Kripke structures
     */
    public Map<Agent, Object> publicAnnouncement(Formula formula, KripkeWorld pointedWorld) throws Exception {
        Map<Agent, Formula> retrievedObservation = new HashMap<>();
        Map<Agent, Object> returns = new HashMap<>();
        for (Map.Entry<Agent, KripkeStructure> entry : this.structures.entrySet()) {
            // announcement
            entry.getValue().publicAnnouncement(formula);

            System.out.println("Agent " + entry.getKey().getName() + " : " + entry.getValue());

            Map<KripkeWorld, Map<Agent, Set<KripkeWorld>>> graph = entry.getValue().getGraph();
            Set<KripkeWorld> set = graph.get(pointedWorld).get(entry.getKey());
            Set<Formula> operands = new HashSet<>();
            for (Map.Entry<Atom, Boolean> entryAssignment : pointedWorld.getAssignment().entrySet()) {
                // we check if the current iterated atom is associated to the agent
                if (atomsAssociation.get(entry.getKey()).contains(entryAssignment.getKey())) {
                    if (entryAssignment.getValue()) {
                        operands.add(entryAssignment.getKey());
                    } else {
                        operands.add(new Not(entryAssignment.getKey()));
                    }
                }
            }

            // we create knowledge formula from the structure for pointed world
            Formula knowledgeFormula = new AgentKnowledge(entry.getKey(), new And(operands));
            if (set.size() > 1) {
                knowledgeFormula = new Not(knowledgeFormula);
            }

            // action and reverse engineering
            Action action = entry.getKey().getAssociatedAction(knowledgeFormula);
            returns.put(entry.getKey(), action.performs(this.objects.get(action).toArray()));
            retrievedObservation.put(entry.getKey(), entry.getKey().reverseEngineering(action));
        }

        // agent self-deduction from other agent program
        for (Map.Entry<Agent, KripkeStructure> entry : this.structures.entrySet()) {
            System.out.println("Agent " + entry.getKey().getName() + " : " + entry.getValue());
            Set<Formula> formulas = new HashSet<>();
            for (Agent destAgent : this.permissions.get(entry.getKey())) { // only if agent knows destAgent program
                formulas.add(retrievedObservation.get(destAgent));
                System.out.println(retrievedObservation.get(destAgent));
            }
            entry.getValue().publicAnnouncement(new And(formulas));
            System.out.println("Agent " + entry.getKey().getName() + " : " + entry.getValue());
        }

        return returns;
    }

    /**
     * Gets the permissions.
     * 
     * @return permissions object
     */
    public Map<Agent, Set<Agent>> getPermissions() {
        return this.permissions;
    }

    /**
     * Gets the objects associated to actions.
     * 
     * @return objects map
     */
    public Map<Action, Set<Object>> getObjects() {
        return this.objects;
    }

    /**
     * Check if the interpreter is terminated.
     * 
     * @param realWorld the real world
     * @return boolean representing the end of the interpreter
     * @throws KripkeStructureInvalidRuntimeException thrown when the real world has
     *                                                been deleted (so no
     *                                                convergence to it)
     */
    public boolean isFinished(KripkeWorld realWorld) throws KripkeStructureInvalidRuntimeException {
        for (KripkeStructure structure : this.structures.values()) {
            if (!structure.getWorlds().contains(realWorld)) {
                throw new KripkeStructureInvalidRuntimeException("Real world not in at least one structure");
            }
            if (structure.getWorlds().size() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the interpreter is terminated.
     * 
     * @return boolean representing the end of the interpreter
     */
    public boolean isFinished() {
        for (KripkeStructure structure : this.structures.values()) {
            if (structure.getWorlds().size() > 1) {
                return false;
            }
        }
        return true;
    }
}
