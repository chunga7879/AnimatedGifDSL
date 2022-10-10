package core;

public interface Node {
    <C, T> T accept(C ctx, NodeVisitor<C, T> v);
}
