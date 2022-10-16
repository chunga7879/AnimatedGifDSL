package core.expressions;

import java.util.Map;

public final class FunctionCall extends Expression {
    private final String identifier;
    private final Map<String, Expression> args;

    public FunctionCall(String identifier, Map<String, Expression> args) {
        this.identifier = identifier;
        this.args = args;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public String identifier() {
        return identifier;
    }

    public Map<String, Expression> args() {
        return args;
    }
}
