package ast.checkers;

import ast.statements.*;
import core.Scope;

import java.util.HashMap;
import java.util.Map;

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
