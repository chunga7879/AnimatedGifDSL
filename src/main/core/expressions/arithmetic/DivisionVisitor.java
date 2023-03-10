package core.expressions.arithmetic;

public class DivisionVisitor extends IntegerArithmeticVisitor {
    private final static String OP = "/";

    public DivisionVisitor() {
        super(DivisionVisitor.OP);
    }

    @Override
    protected int apply(int a, int b) {
        return a / b;
    }
}
