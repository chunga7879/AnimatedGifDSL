package core.expressions.comparison;

public class GTEVisitor extends IntegerComparisonVisitor {
    private static final String OP = ">=";

    public GTEVisitor() {
        super(GTEVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a >= b;
    }
}
