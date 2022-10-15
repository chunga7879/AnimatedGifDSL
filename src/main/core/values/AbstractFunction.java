package core.values;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.exceptions.DSLException;
import core.expressions.ExpressionVisitor;

import java.util.Map;

public abstract class AbstractFunction extends Value {
    public static final String NAME = "Function";
    public static final String PARAM_TARGET = "$target";
    public static final String PARAM_ON = "$on";

    public AbstractFunction() {
        super(AbstractFunction.NAME);
    }

    public abstract Value call(Scope scope);

    @Override
    public AbstractFunction asFunction() {
        return this;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }


    public abstract String getFunctionName();

    /**
     * Check the function arguments of the function
     * @param scope
     * @return A value with the return type of the function
     */
    public Value checkArgs(Scope scope) {
        try {
            ArgumentChecker.check(scope, getParams(), getFunctionName());
        } catch (DSLException e) {
            throw e.withPosition(this);
        }
        return checkReturn();
    }

    /**
     * Return a value with the return type of the function
     * @return
     */
    public abstract Value checkReturn();

    /**
     * Get parameters of the function
     * @return
     */
    public abstract Map<String, String> getParams();
}
