package core.expressions.comparison;

import core.Scope;
import core.expressions.Expression;
import core.values.BooleanValue;
import core.values.StringValue;

public class NEVisitor extends IntegerComparisonVisitor {
    private static final String OP  = "!=";

    public NEVisitor() {
        super(NEVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a != b;
    }
}
