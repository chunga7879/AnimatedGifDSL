package core.expressions.arithmetic;

public class MultiplicationVisitor extends IntegerArithmeticVisitor {
    private final static String OP = "*";

    public MultiplicationVisitor(String op) {
        super(MultiplicationVisitor.OP);
    }

    @Override
    protected int apply(int a, int b) {
        return a * b;
    }
}
