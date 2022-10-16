package core.expressions;

import core.Node;

public abstract class Expression extends Node {
    public abstract <C, T> T accept(C ctx, ExpressionVisitor<C, T> v);
}
