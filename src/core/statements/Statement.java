package core.statements;

public interface Statement {
    <C, T> T accept(C ctx, StatementVisitor<C, T> v);
}
