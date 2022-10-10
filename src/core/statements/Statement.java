package core.statements;

import core.Scope;

public interface Statement {
    void Do(Scope s);

    <C, T> T accept(C ctx, StatementVisitor<C, T> v);
}
