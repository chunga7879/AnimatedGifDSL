package core.expressions.arithmetic;

public class SubtractionVisitor extends IntegerArithmeticVisitor {
    private final static String OP = "-";

    public SubtractionVisitor() {
        super(SubtractionVisitor.OP);
    }

    @Override
    protected int apply(int a, int b) {
        return a - b;
    }
}
