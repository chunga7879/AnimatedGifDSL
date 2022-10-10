package core.expressions.comparison;

import core.Scope;
import core.expressions.Expression;
import core.values.BooleanValue;
import core.values.StringValue;
import core.values.Value;

public class EQVisitor extends IntegerComparisonVisitor {
    private static final String OP = "==";

    public EQVisitor() {
        super(EQVisitor.OP);
    }

    @Override
    public BooleanValue visit(StringValue a, Value b, Scope s) {
        return new BooleanValue(a.get().equals(b.asString().get()));
    }

    @Override
    protected boolean apply(int a, int b) {
        return a == b;
    }
}
