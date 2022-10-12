package core;

import core.exceptions.NameError;
import core.values.AbstractFunction;
import core.values.Value;

import java.util.HashMap;
import java.util.Objects;

public class Scope {
    private static Scope global;
    private final Scope parent;
    private HashMap<String, Value> vars;


    public Scope() {
        this(null);
    }

    private Scope(Scope parent) {
        this.parent = parent;
        this.vars = new HashMap<>();
    }

    private Boolean hasParent() {
        return this.parent != null;
    }

    public Scope newChildScope() {
        return new Scope(this);
    }

    public Value getVar(String name) {
        Value v = this.vars.get(name);
        if (v != null) {
            return v;
        }
        if (this.hasParent()) {
            return this.parent.getVar(name);
        }
        throw new NameError(name);
    }

    public void setVar(String name, Value v) {
        // TODO: might want to split storing for values and functions
        if (hasVar(name)) {
            Value prevVal = getVar(name);
            if (Objects.equals(prevVal.getTypeName(), AbstractFunction.NAME)) throw new RuntimeException("Cannot redefine function: " + name);
        }
        if (this.hasParent() && this.parent.hasVar(name)) {
            this.parent.setVar(name, v);
        } else {
            this.vars.put(name, v);
        }
    }

    /**
     * Check if variable exists
     * @param name
     * @return
     */
    public boolean hasVar(String name) {
        if (hasVarLocal(name)) return true;
        if (this.hasParent()) return this.parent.hasVar(name);
        return false;
    }

    /**
     * Check if variable exists in local scope
     * @param name
     * @return
     */
    public boolean hasVarLocal(String name) {
        return vars.containsKey(name);
    }

    /**
     * Get size of local scope
     * @return
     */
    public int getLocalSize() {
        return vars.size();
    }

    public String getLocalKeyString() {
        return "(" + String.join(", ", this.vars.keySet()) + ")";
    }
}
