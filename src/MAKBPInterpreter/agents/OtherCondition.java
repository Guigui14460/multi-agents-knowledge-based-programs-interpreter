package MAKBPInterpreter.agents;

import java.util.Map;

import MAKBPInterpreter.logic.Atom;
import MAKBPInterpreter.logic.Formula;

// TODO: se in can be used in the agent conditions map
final public class OtherCondition implements Formula {
    static OtherCondition instance = new OtherCondition();

    private OtherCondition() {
    }

    @Override
    public Formula simplify() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Formula getNegation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(Formula otherFormula) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean evaluate(Map<Atom, Boolean> state, Object... objects) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }
}
