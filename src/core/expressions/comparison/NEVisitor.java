package core.expressions.comparison;

public class NEVisitor extends IntegerComparisonVisitor {
    private static final String OP = "!=";

    public NEVisitor() {
        super(NEVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a != b;
    }
}
