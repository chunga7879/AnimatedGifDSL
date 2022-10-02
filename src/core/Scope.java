package core;

import builtin.functions.Print;
import core.exceptions.NameError;
import core.values.Value;

import java.util.HashMap;

public class Scope {
    private HashMap<String, Value> vars;
    private final Scope parent;
    private static Scope global;


    private Scope() {
        this(getGlobalScope());
    }
    private Scope(Scope parent) {
        this.parent = parent;
        this.vars = new HashMap<>();
    }

    public static Scope getGlobalScope() {
        if (Scope.global == null) {
            Scope.global = new Scope(null);
            // TODO add built in functions here.
            Scope.global.setVar("PRINT", new Print());
        }
        return Scope.global;
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
}
