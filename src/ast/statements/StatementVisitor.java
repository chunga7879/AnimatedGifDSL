package ast.statements;

public interface StatementVisitor<C, T> {
    T visit(C ctx, FunctionDef f);

    T visit(C ctx, FunctionCall fc);

    T visit(C ctx, Loop loop);

    T visit(C ctx, IfStatement ifs);
}
