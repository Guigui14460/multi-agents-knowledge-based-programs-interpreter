package MAKBPInterpreter.agents;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MAKBPInterpreter.logic.Formula;

/**
 * Class representing an ordered map specifically implemented to handle agent
 * program.
 */
public class AgentProgram implements Map<Formula, Action> {
    /**
     * List of ordered keys.
     */
    private List<Formula> keys;

    /**
     * List of ordered action associated to {@code keys}.
     */
    private List<Action> values;

    /**
     * Constructor.
     * 
     * The argument were copied to a new array to avoid outside modifications.
     * 
     * @param keys   list of keys
     * @param values list of associated values to the keys
     */
    public AgentProgram(List<Formula> keys, List<Action> values) {
        this.keys = new ArrayList<>(keys);
        this.values = new ArrayList<>(values);
    }

    /**
     * Copy constructor.
     * 
     * @param other other program to copy
     */
    public AgentProgram(AgentProgram other) {
        this(other.keys, other.values);
    }

    /**
     * Default constructor.
     * Initialize the two inner lists at an empty array.
     */
    public AgentProgram() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AgentProgram) {
            AgentProgram program = (AgentProgram) other;
            return program.keys.equals(this.keys) && program.values.equals(this.values);
        }
        return false;
    }

    @Override
    public void clear() {
        this.keys.clear();
        this.values.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.values.contains(value);
    }

    @Override
    public Set<Entry<Formula, Action>> entrySet() {
        Set<Entry<Formula, Action>> entries = new HashSet<>();
        for (int i = 0; i < this.keys.size(); i++) {
            entries.add(new AbstractMap.SimpleEntry<Formula, Action>(this.keys.get(i), this.values.get(i)));
        }
        return entries;
    }

    @Override
    public Action get(Object key) {
        int index = this.keys.indexOf(key);
        return this.values.get(index);
    }

    /**
     * Gets the key (formula) at a specified index.
     * 
     * @param index specified index
     * @return associated key of the index
     */
    public Formula getKey(int index) {
        return this.keys.get(index);
    }

    /**
     * Gets the value (action) at a specified index.
     * 
     * @param index specified index
     * @return associated action of the index
     */
    public Action getValue(int index) {
        return this.values.get(index);
    }

    /**
     * Gets the index of a key.
     * 
     * @param arg0 key to get the index of it
     * @return index of the given key
     */
    public int getIndex(Formula arg0) {
        return this.keys.indexOf(arg0);
    }

    @Override
    public boolean isEmpty() {
        return this.keys.isEmpty();
    }

    @Override
    public Set<Formula> keySet() {
        return new HashSet<>(this.keys);
    }

    /**
     * Inserts a key and associated value to a specific index. If the key is already
     * in the map, the key is remove from the list and reinsert at the given index.
     * If specified index is greater than the size of map, we put the couple
     * key/value at the end of the map.
     * 
     * If the given key is null, we put at the end or if the given index is the last
     * index of the list, we check if we need to move the null element at the end.
     * 
     * @param index index to place key and value
     * @param key   key to insert
     * @param value value to insert
     * @return index that the key/value inserted
     */
    public int insert(int index, Formula key, Action value) {
        // put directly at the end
        if (key == null) {
            this.put(key, value);
            return this.size() - 1;
        }

        if (this.containsKey(key)) {
            this.remove(key);
        }

        // if index out of bounds, we place it before the null element or at the end if
        // null is not present
        if (index >= this.size()) {
            int index2 = this.size() - 1;
            if (this.containsKey(null)) {
                index2--;
            }
            return this.insert(index2, key, value);
        }

        this.keys.add(index, key);
        this.values.add(index, value);
        return index;
    }

    @Override
    public Action put(Formula arg0, Action arg1) {
        if (arg1 == null) {
            throw new NullPointerException("An action cannot be null");
        }

        // if key is null, put associated action at the end
        if (arg0 == null) {
            if (!this.containsKey(arg0)) {
                this.keys.add(arg0);
                this.values.add(arg1);
                return null;
            }

            int index = this.size() - 1;
            Action action = this.values.get(index);
            this.values.set(index, arg1);
            return action;
        }

        // if key is not null
        if (this.containsKey(arg0)) {
            int index = this.keys.indexOf(arg0);
            Action action = this.values.get(index);
            this.values.set(index, arg1);
            return action;
        }

        this.keys.add(arg0);
        this.values.add(arg1);
        if (this.containsKey(null)) {
            Action nullAction = this.remove(null);
            this.keys.add(null);
            this.values.add(nullAction);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends Formula, ? extends Action> m) {
        throw new UnsupportedOperationException("Use the other putAll method with two lists");
    }

    /**
     * Puts all the given keys and associated values to the map object.
     * 
     * @param arg0 list of keys
     * @param arg1 list of associated values to the keys
     */
    public void putAll(List<Formula> arg0, List<Action> arg1) {
        if (arg0.size() != arg1.size()) {
            throw new IllegalArgumentException("The two lists need to have the exact same size");
        }
        for (int i = 0; i < arg0.size(); i++) {
            this.put(arg0.get(i), arg1.get(i));
        }
    }

    @Override
    public Action remove(Object key) {
        if (key != null && !(key instanceof Formula)) {
            throw new ClassCastException();
        }

        if (!this.containsKey(key)) {
            throw new IllegalArgumentException("Key not in map");
        }

        int index = this.keys.indexOf(key);
        Action value = this.values.get(index);
        this.keys.remove(index);
        this.values.remove(index);
        return value;
    }

    @Override
    public int size() {
        return this.keys.size();
    }

    @Override
    public Collection<Action> values() {
        return new HashSet<>(this.values);
    }

    /**
     * Gets all formulas before a specified index. Given index excluded.
     * If {@code index == 0}, an empty array is returned.
     * 
     * @param index specified index
     * @return sublist of keys from 0 included to {@code index} excluded.
     */
    public List<Formula> getKeysBeforeAt(int index) {
        if (index == 0) {
            return new ArrayList<>();
        }
        if (index > this.size()) {
            index = this.size();
        }
        return this.keys.subList(0, index);
    }

    /**
     * Gets the keys in the right order.
     * 
     * @return ordered keys
     */
    public List<Formula> getOrderedKeys() {
        return new ArrayList<>(this.keys);
    }

    /**
     * Gets the values in the right order.
     * 
     * @return ordered values
     */
    public List<Action> getOrderedValues() {
        return new ArrayList<>(this.values);
    }
}