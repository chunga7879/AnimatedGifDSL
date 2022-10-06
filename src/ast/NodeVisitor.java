package ast;

public interface NodeVisitor<C, T> {
    T visit(C ctx, Program p);
}
