package MAKBPInterpreter.agents;

/**
 * Store a pair of {@code KripkeWorld} and {@code KripStructure}
 */
public class Kripke {

    private KripkeWorld world;
    private KripkeStructure struct;

    public Kripke(KripkeWorld world, KripkeStructure struct) {
        this.world = world;
        this.struct = struct;
    }

    public KripkeWorld getWorld() {
        return this.world;
    }

    public void setWorld(KripkeWorld world) {
        this.world = world;
    }

    public KripkeStructure getStruct() {
        return this.struct;
    }

    public void setStruct(KripkeStructure struct) {
        this.struct = struct;
    }

}
