package MAKBPInterpreter.interpreter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MAKBPInterpreter.agents.Action;
import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.agents.KripkeStructure;
import MAKBPInterpreter.agents.KripkeWorld;
import MAKBPInterpreter.agents.exceptions.KripkeStructureInvalidRuntimeException;
import MAKBPInterpreter.agents.exceptions.NoKripkeWorldPossibleException;
import MAKBPInterpreter.logic.And;
import MAKBPInterpreter.logic.Formula;

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
    private Map<Action, List<Object>> objects;

    /**
     * Constructor.
     * 
     * @param agents      set of agents
     * @param structure   default structure to copy for each agents
     * @param permissions agent programs knowledge for each agent
     * @param objects     lists of objects to pass for action execution
     */
    public MAKBPInterpreter(Set<Agent> agents, KripkeStructure structure,
            Map<Agent, Set<Agent>> permissions, Map<Action, List<Object>> objects) {
        this.structures = new HashMap<>();
        for (Agent agent : agents) {
            KripkeStructure newStructure = new KripkeStructure(structure);
            this.structures.put(agent, newStructure);
        }
        this.permissions = permissions;
        this.objects = objects;
    }

    /**
     * Announcement of associated formulas to agents.
     * 
     * Specification to announce formulas to agents in one method.
     * 
     * @param agents   list of agents who received announcement
     * @param formulas list of formula to announce
     * @throws Exception thrown when the list sizes are not equals or by the Kripke
     *                   structure in case of error
     */
    public void publicAnnouncement(List<Agent> agents, List<Formula> formulas) throws Exception {
        if (agents.size() != formulas.size()) {
            throw new IllegalArgumentException("agents and formulas object need to have exact same size");
        }
        for (int i = 0; i < agents.size(); i++) {
            this.publicAnnouncement(agents.get(i), formulas.get(i));
        }
    }

    /**
     * Announcement of associated formulas to agents.
     * 
     * Specification to announce formula to agent via a map.
     * 
     * @param formulas map of formula to announce to a related agent
     * @throws Exception thrown when the list sizes are not equals or by the Kripke
     *                   structure in case of error
     */
    public void publicAnnouncement(Map<Agent, Formula> formulas) throws Exception {
        for (Map.Entry<Agent, Formula> entry : formulas.entrySet()) {
            this.publicAnnouncement(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Announcement of a formula to a collection of agents.
     * 
     * Specification to announce one formula to multiple agents.
     * 
     * @param agents  collection of agents who received announcement
     * @param formula formula to announce
     * @throws Exception thrown by the Kripke structure in case of error
     */
    public void publicAnnouncement(Collection<Agent> agents, Formula formula) throws Exception {
        for (Agent agent : agents) {
            this.publicAnnouncement(agent, formula);
        }
    }

    /**
     * Announcement of a formula to an agent.
     * 
     * @param agent   agent who received announcement
     * @param formula formula to announce
     * @throws Exception thrown by the Kripke structure in case of error
     */
    public void publicAnnouncement(Agent agent, Formula formula) throws Exception {
        this.structures.get(agent).publicAnnouncement(formula);
    }

    /**
     * Gets the associated action for each agent of the {@code agents} argument by
     * retrieving its associated Kripke structure and the {@code pointedWorld}.
     * 
     * @param agents       collection of agents to get actions
     * @param pointedWorld pointed world in the Kripke structure to retrieve actions
     * @return map of agents and actions
     * @throws Exception throws when the formula not supported evaluate operation or
     *                   expected object not given
     */
    public Map<Agent, Action> getAssociatedAction(Collection<Agent> agents, KripkeWorld pointedWorld) throws Exception {
        Map<Agent, Action> actions = new HashMap<>();
        for (Agent agent : agents) {
            Action action = agent.getAssociatedAction(this.structures.get(agent), pointedWorld);
            actions.put(agent, action);
        }
        return actions;
    }

    /**
     * Executes retrieving actions.
     * 
     * @param actions actions to execute
     * @return returned objects of the actions executions
     * @throws Exception thrown when an actions no longer exists in {@code objects}
     *                   attribute or when an action received illegal arguments,
     *                   objects cannot be processed, etc
     */
    public Map<Agent, Object> executeAction(Map<Agent, Action> actions) throws Exception {
        Map<Agent, Object> returns = new HashMap<>();
        for (Map.Entry<Agent, Action> entry : actions.entrySet()) {
            if (!this.objects.containsKey(entry.getValue())) {
                throw new NullPointerException("objects doesn't have key '" + entry.getValue() + "'");
            }
            Object r = entry.getValue().performs(this.objects.get(entry.getValue()).toArray());
            returns.put(entry.getKey(), r);
        }
        return returns;
    }

    /**
     * Gets modal logic formulas for each retrieved action.
     * 
     * @param actions retrieved actions
     * @return modal logic formulas
     */
    public Map<Agent, Formula> reverseEngineering(Map<Agent, Action> actions) {
        Map<Agent, Formula> observations = new HashMap<>();
        for (Map.Entry<Agent, Action> entry : actions.entrySet()) {
            Formula f = entry.getKey().reverseEngineering(entry.getValue());
            observations.put(entry.getKey(), f);
        }
        return observations;
    }

    /**
     * Returns a formula in function of observations and agents programs knowledge
     * for an agent.
     * 
     * @param agent        reasoning agent
     * @param observations observations
     * @return deducted formula
     * 
     * @see #reasoning(Agent, Map, Map)
     * @note Use this method if your observations knowledge is the same as your
     *       agents programs knowledge.
     */
    public Formula reasoning(Agent agent, Map<Agent, Formula> observations) {
        return this.reasoning(agent, observations, this.permissions);
    }

    /**
     * Returns a formula in function of observations and observations knowledge for
     * agents via permissions.
     * 
     * @param agent        reasoning agent
     * @param observations observations
     * @param permissions  map to know if an agent can see another agent
     * @return deducted formula
     */
    public Formula reasoning(Agent agent, Map<Agent, Formula> observations, Map<Agent, Set<Agent>> permissions) {
        Set<Formula> formulas = new HashSet<>();
        for (Agent destAgent : this.permissions.get(agent)) { // only if agent can see destAgent
            formulas.add(observations.get(destAgent));
        }
        return new And(formulas);
    }

    /**
     * Returns a map of agents and formulas in function of observations and agents
     * programs knowledge for agents.
     * 
     * @param agents       reasoning agents
     * @param observations observations
     * @return deducted formulas
     * @see #reasoning(Collection, Map, Map)
     * @note Use this method if your observations knowledge is the same as your
     *       agents programs knowledge.
     */
    public Map<Agent, Formula> reasoning(Collection<Agent> agents, Map<Agent, Formula> observations) {
        return this.reasoning(agents, observations, this.permissions);
    }

    /**
     * Returns a map of agents and formulas in function of observations and
     * observations knowledge for agents via permissions.
     * 
     * @param agents       reasoning agents
     * @param observations observations
     * @return deducted formulas
     */
    public Map<Agent, Formula> reasoning(Collection<Agent> agents, Map<Agent, Formula> observations,
            Map<Agent, Set<Agent>> permissions) {
        Map<Agent, Formula> formulas = new HashMap<>();
        for (Agent agent : agents) {
            Formula f = this.reasoning(agent, observations, permissions);
            formulas.put(agent, f);
        }
        return formulas;
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
    public Map<Action, List<Object>> getObjects() {
        return this.objects;
    }

    /**
     * Gets the Kripke structures of the agents.
     * 
     * @return structures map
     */
    public Map<Agent, KripkeStructure> getStructures() {
        return this.structures;
    }

    /**
     * Check if the interpreter is terminated.
     * 
     * @param realWorld the real world
     * @return boolean representing the end of the interpreter
     * @throws KripkeStructureInvalidRuntimeException thrown when the real world has
     *                                                been deleted (so no
     *                                                convergence to it)
     * @thorws NoKripkeWorldPossibleException thrown when no world are in a
     *         structure (impossible, need one real world)
     */
    public boolean isFinished(KripkeWorld realWorld)
            throws KripkeStructureInvalidRuntimeException, NoKripkeWorldPossibleException {
        for (Map.Entry<Agent, KripkeStructure> entry : this.structures.entrySet()) {
            if (entry.getValue().getWorlds().size() == 0) {
                throw new NoKripkeWorldPossibleException();
            }

            if (!entry.getValue().getWorlds().contains(realWorld)) {
                throw new KripkeStructureInvalidRuntimeException("Real world not in at least one structure");
            }

            if (entry.getValue().getWorlds().size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the interpreter is terminated.
     * 
     * @return boolean representing the end of the interpreter
     * @thorws NoKripkeWorldPossibleException thrown when no world are in a
     *         structure (impossible, need one real world)
     */
    public boolean isFinished() throws NoKripkeWorldPossibleException {
        for (KripkeStructure structure : this.structures.values()) {
            if (structure.getWorlds().size() == 0) {
                throw new NoKripkeWorldPossibleException();
            }

            if (structure.getWorlds().size() > 1) {
                return false;
            }
        }
        return true;
    }
}
