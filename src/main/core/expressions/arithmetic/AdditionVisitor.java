package core.expressions.arithmetic;

public class AdditionVisitor extends IntegerArithmeticVisitor {
    private final static String OP = "+";

    public AdditionVisitor() {
        super(AdditionVisitor.OP);
    }

    @Override
    protected int apply(int a, int b) {
        return a + b;
    }
}
