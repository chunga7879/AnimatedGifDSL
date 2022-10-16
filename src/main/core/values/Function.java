package core.values;

import core.Scope;
import core.exceptions.InternalException;
import core.expressions.ExpressionVisitor;
import core.statements.Statement;

import java.util.List;
import java.util.Map;

// A user defined function (or main).
public class Function extends AbstractFunction {
    private static final String ACTUAL_NAME = "User-Defined Function";
    private final List<Statement> statements;
    private final Map<String, String> params;

    public Function(List<Statement> statements, Map<String, String> params) {
        this.statements = statements;
        this.params = params;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public Value call(Scope scope) {
        throw new InternalException("remove me");
    }

    /**
     * Get name of the function
     * @return
     */
    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Value checkReturn() {
        return new Unknown();
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
