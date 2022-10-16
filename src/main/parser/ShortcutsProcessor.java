package parser;

import core.Scope;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.statements.*;
import core.values.AbstractFunction;
import core.values.Null;
import core.values.Unknown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Processor to enable shortcuts
 */
public class ShortcutsProcessor implements StatementVisitor<Scope, Statement> {
    @Override
    public Program visit(Scope ctx, Program program) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : program.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new Program(statements);
    }

    @Override
    public FunctionDefinition visit(Scope ctx, FunctionDefinition fd) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : fd.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new FunctionDefinition(fd.name(), statements, fd.params());
    }

    @Override
    public IfStatement visit(Scope ctx, IfStatement is) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : is.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new IfStatement(is.cond(), statements);
    }

    @Override
    public LoopStatement visit(Scope ctx, LoopStatement ls) {
        List<Statement> statements = new ArrayList<>();
        for (Statement statement : ls.statements()) {
            statements.add(statement.accept(ctx, this));
        }
        return new LoopStatement(ls.array(), ls.loopVar(), statements);
    }

    @Override
    public Return visit(Scope ctx, Return r) {
        return r;
    }

    @Override
    public VariableAssignment visit(Scope ctx, VariableAssignment va) {
        return va;
    }

    @Override
    public Statement visit(Scope ctx, ExpressionWrapper ew) {
        // Only for predefined functions:
        // If you use `FUNCTION x` for a function that returns a value
        // It'll automatically do `FUNCTION x as x`
        if (ew.e() instanceof FunctionCall functionCall && ctx.hasVar(functionCall.identifier())) {
            AbstractFunction function = ctx.getVar(functionCall.identifier()).asFunction();

            if (checkFunctionReturnMatchesTypeParameterType(function, AbstractFunction.PARAM_ON) &&
                functionCall.args().containsKey(AbstractFunction.PARAM_ON)) {
                Expression arg = functionCall.args().get(AbstractFunction.PARAM_ON);
                if (arg instanceof VariableExpression variableArg) {
                    return new VariableAssignment(variableArg.identifier(), functionCall);
                }
            }
            if (!function.getParams().containsKey(AbstractFunction.PARAM_ON) &&
                checkFunctionReturnMatchesTypeParameterType(function, AbstractFunction.PARAM_TARGET) &&
                functionCall.args().containsKey(AbstractFunction.PARAM_TARGET)) {
                Expression arg = functionCall.args().get(AbstractFunction.PARAM_TARGET);
                if (arg instanceof VariableExpression variableArg) {
                    return new VariableAssignment(variableArg.identifier(), functionCall);
                }
            }
        }
        return ew;
    }

    private static boolean checkFunctionReturnMatchesTypeParameterType(AbstractFunction function, String parameterName) {
        if (!function.getParams().containsKey(parameterName)) return false;
        String parameterType = function.getParams().get(parameterName);
        return parameterType != null && // Type cannot be null
            !Objects.equals(parameterType, Unknown.NAME) && // Type cannot be unknown
            function.checkReturn() != null && // Function has return
            !Objects.equals(function.checkReturn().getTypeName(), null) && // Function return value has type
            !Objects.equals(function.checkReturn().getTypeName(), Null.NULL.getTypeName()) && // Function does not return null
            !Objects.equals(function.checkReturn().getTypeName(), Unknown.NAME) && // Function does not return unknown
            Objects.equals(function.checkReturn().getTypeName(), parameterType); // Function return matches type
    }
}
