package ast.evaluators;

import ast.statements.*;
import core.Scope;

public class StatementEvaluator implements StatementVisitor<Scope, Void> {
    private final Scope scope;
    private final ExpressionEvaluator ee;

    public StatementEvaluator() {
        this.scope = new Scope();
        this.ee = new ExpressionEvaluator();
        // TODO add functions
    }

    @Override
    public Void visit(Scope ctx, FunctionDef f) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, FunctionCall fc) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, Loop loop) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, IfStatement ifs) {
        // TODO once bool value is added change this
        if (ifs.cond().accept(this.scope, this.ee).asString().get().equals("TRUE")) {
            for (StatementNode s : ifs.body()) {
                s.accept(this.scope, this);
            }
        }
        return null;
    }
}
