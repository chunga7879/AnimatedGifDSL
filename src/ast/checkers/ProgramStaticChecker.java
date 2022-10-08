package ast.checkers;

import ast.NodeVisitor;
import ast.Program;
import ast.statements.StatementNode;
import core.Scope;

public class ProgramStaticChecker implements NodeVisitor<Scope, Void> {
    @Override
    public Void visit(Scope ctx, Program p) {
        return null;
    }
}
