package ast.checkers;

import ast.statements.FunctionDef;
import ast.statements.IfStatement;
import ast.statements.Loop;
import ast.statements.StatementVisitor;
import core.Scope;

public class StatementStaticChecker implements StatementVisitor<Scope, Void> {

    @Override
    public Void visit(Scope ctx, FunctionDef f) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, Loop loop) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, IfStatement ifs) {
        return null;
    }
}
