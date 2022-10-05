package core.expressions.comparison;

public class LTEVisitor extends IntegerComparisonVisitor {
    private static final String OP  = "<=";

    public LTEVisitor() {
        super(LTEVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a <= b;
    }
}
