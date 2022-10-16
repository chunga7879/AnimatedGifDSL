package core.statements;

import core.expressions.Expression;

import java.util.List;

public final class LoopStatement extends Statement {
    private final Expression array;
    private final String loopVar;
    private final List<Statement> statements;

    public LoopStatement(Expression array, String loopVar, List<Statement> statements) {
        this.array = array;
        this.loopVar = loopVar;
        this.statements = statements;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public Expression array() {
        return array;
    }

    public String loopVar() {
        return loopVar;
    }

    public List<Statement> statements() {
        return statements;
    }
}
