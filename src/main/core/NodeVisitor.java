package core;

import core.values.Function;

public interface NodeVisitor<C, T> {
    T visit(C ctx, Function f);
}
