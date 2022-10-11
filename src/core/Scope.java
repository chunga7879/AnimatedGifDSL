package core;

import core.exceptions.NameError;
import core.values.Value;

import java.util.HashMap;

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
        if (this.hasParent() && this.parent.vars.containsKey(name)) {
            this.parent.setVar(name, v);
        } else {
            this.vars.put(name, v);
        }
    }

    public boolean hasVar(String name) {
        return vars.containsKey(name);
    }
}
