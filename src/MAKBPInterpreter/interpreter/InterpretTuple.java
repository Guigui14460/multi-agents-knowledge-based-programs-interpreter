package MAKBPInterpreter.interpreter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import MAKBPInterpreter.agents.Agent;
import MAKBPInterpreter.logic.Formula;

public class InterpretTuple {
    public boolean isEmpty = false;
    public boolean initialObservationsIntegrated;
    public Map<Agent, Object> actionsReturns;
    public Map<Agent, Formula> deductedFormulas;

    public InterpretTuple(Map<Agent, Object> actionsReturns, Map<Agent, Formula> deductedFormulas,
            boolean initialObservationsIntegrated) {
        this.initialObservationsIntegrated = initialObservationsIntegrated;
        this.actionsReturns = actionsReturns;
        this.deductedFormulas = deductedFormulas;
    }

    public InterpretTuple(Collection<Agent> agents, boolean initialObservationsIntegrated) {
        this.initialObservationsIntegrated = initialObservationsIntegrated;
        this.actionsReturns = new HashMap<>();
        this.deductedFormulas = new HashMap<>();
        for (Agent agent : agents) {
            this.actionsReturns.put(agent, null);
            this.deductedFormulas.put(agent, null);
        }
    }

    public InterpretTuple(boolean initialObservationsIntegrated) {
        this.isEmpty = true;
        this.initialObservationsIntegrated = initialObservationsIntegrated;
        this.actionsReturns = new HashMap<>();
        this.deductedFormulas = new HashMap<>();
    }

    public InterpretTuple(InterpretTuple other) {
        this.isEmpty = other.isEmpty;
        this.initialObservationsIntegrated = other.initialObservationsIntegrated;
        this.actionsReturns = new HashMap<>(other.actionsReturns);
        this.deductedFormulas = new HashMap<>(other.deductedFormulas);
    }
}