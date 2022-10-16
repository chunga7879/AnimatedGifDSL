package core.statements;

import core.Node;

public abstract class Statement extends Node {
    public abstract <C, T> T accept(C ctx, StatementVisitor<C, T> v);
}
