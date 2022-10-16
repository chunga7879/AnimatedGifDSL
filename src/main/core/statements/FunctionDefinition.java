package core.statements;

import java.util.HashMap;
import java.util.List;

public record FunctionDefinition(String name, List<Statement> statements, HashMap<String, String> params) implements Statement {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
