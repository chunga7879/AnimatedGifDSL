package core.statements;

import java.util.HashMap;
import java.util.List;

public final class FunctionDefinition extends Statement {
    private final String name;
    private final List<Statement> statements;
    private final HashMap<String, String> params;

    public FunctionDefinition(String name, List<Statement> statements, HashMap<String, String> params) {
        this.name = name;
        this.statements = statements;
        this.params = params;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public String name() {
        return name;
    }

    public List<Statement> statements() {
        return statements;
    }

    public HashMap<String, String> params() {
        return params;
    }
}
