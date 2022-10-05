package core.expressions.comparison;

public class LTVisitor extends IntegerComparisonVisitor {
    private static final String OP  = "<";

    public LTVisitor() {
        super(LTVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a < b;
    }
}
