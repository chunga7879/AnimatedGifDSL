package core.expressions.comparison;

public class GTVisitor extends IntegerComparisonVisitor {
    private static final String OP  = ">";

    public GTVisitor() {
        super(GTVisitor.OP);
    }

    @Override
    protected boolean apply(int a, int b) {
        return a > b;
    }
}
