package parser;

import core.Scope;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.statements.*;
import core.values.AbstractFunction;
import core.values.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Temp class to enable shortcuts
 * Do be updated
 */
public class ShortcutsProcessor implements StatementVisitor<Scope, Statement> {

    public Function visit(Scope ctx, Function func) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : func.getStatements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new Function(statements, func.getParams());
    }

    @Override
    public Statement visit(Scope ctx, FunctionDefinition fd) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : fd.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new FunctionDefinition(fd.name(), statements, fd.params());
    }

    @Override
    public Statement visit(Scope ctx, IfStatement is) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : is.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new IfStatement(is.cond(), statements);
    }

    @Override
    public Statement visit(Scope ctx, LoopStatement ls) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : ls.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new LoopStatement(ls.array(), ls.loopVar(), statements);
    }

    @Override
    public Statement visit(Scope ctx, Return r) {
        return r;
    }

    @Override
    public Statement visit(Scope ctx, VariableAssignment va) {
        return va;
    }

    @Override
    public Statement visit(Scope ctx, ExpressionWrapper ew) {
        // Only for predefined functions:
        // If you use `FUNCTION x` for a function that returns a value
        // It'll automatically do `FUNCTION x as x`
        if (ew.e() instanceof FunctionCall functionCall && ctx.hasVar(functionCall.identifier())) {
            AbstractFunction function = ctx.getVar(functionCall.identifier()).asFunction();
            if (function.getParams().containsKey(AbstractFunction.PARAM_TARGET)) {
                String type = function.getParams().get(AbstractFunction.PARAM_TARGET);
                if (Objects.equals(function.checkReturn().getTypeName(), type) &&
                    functionCall.args().containsKey(AbstractFunction.PARAM_TARGET)) {
                    Expression arg = functionCall.args().get(AbstractFunction.PARAM_TARGET);
                    if (arg instanceof VariableExpression variableArg) {
                        return new VariableAssignment(variableArg.identifier(), functionCall);
                    }
                }
            }
        }
        return ew;
    }
}
