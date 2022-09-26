package core.values;

import core.Scope;
import core.exceptions.TypeError;
import core.expressions.Expression;

public abstract class Value implements Expression {
    private final String typeName;

    public Value(String typeName) {
        this.typeName = typeName;
    }

    public StringValue asString() {
        throw new TypeError(this.typeName, StringValue.NAME);
    }

    public AbstractFunction asFunction() {
        throw new TypeError(this.typeName, Function.NAME);
    }

    public Array asArray() {
        throw new TypeError(this.typeName, Array.NAME);
    }

    public Colour asColour() {
        throw new TypeError(this.typeName, Colour.NAME);
    }

    @Override
    public Value evaluate(Scope s) {
        return this;
    }
}
