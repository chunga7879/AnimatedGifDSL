package ast.evaluators;

import ast.NodeVisitor;
import ast.Program;
import core.Scope;


public class ProgramEvaluator implements NodeVisitor<Scope, Void> {
    @Override
    public Void visit(Scope ctx, Program p) {
        return null;
    }
}
