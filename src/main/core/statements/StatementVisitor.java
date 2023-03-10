package core.statements;

public interface StatementVisitor<C, T> {

    T visit(C ctx, Program program);

    T visit(C ctx, FunctionDefinition fd);

    T visit(C ctx, IfStatement is);

    T visit(C ctx, LoopStatement ls);

    T visit(C ctx, Return r);

    T visit(C ctx, VariableAssignment va);

    T visit(C ctx, ExpressionWrapper ew);
}
