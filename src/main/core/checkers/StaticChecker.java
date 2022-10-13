package core.checkers;

import core.Scope;
import core.exceptions.FunctionException;
import core.exceptions.TypeError;
import core.exceptions.VariableException;
import core.expressions.*;
import core.statements.*;
import core.values.*;

import java.util.Map;
import java.util.Objects;

public class StaticChecker implements ExpressionVisitor<Scope, Value>, StatementVisitor<Scope, Value> {

    @Override
    public Value visit(Scope ctx, ArithmeticExpression ae) {
        String expressionA = ae.a().accept(ctx, this).getTypeName();
        String expressionB = ae.b().accept(ctx, this).getTypeName();
        if (!(expressionA.equals(IntegerValue.NAME) || expressionA.equals(Unknown.NAME)) ||
            !(expressionB.equals(IntegerValue.NAME) || expressionB.equals(Unknown.NAME))) {
            throw new TypeError("Invalid arithmetic expression");
        }
        return new IntegerValue(0);
    }

    @Override
    public Value visit(Scope ctx, ComparisonExpression ce) {
        String expressionA = ce.a().accept(ctx, this).getTypeName();
        String expressionB = ce.b().accept(ctx, this).getTypeName();
        if (!(expressionA.equals(IntegerValue.NAME) || expressionA.equals(Unknown.NAME)) ||
            !(expressionB.equals(IntegerValue.NAME) || expressionB.equals(Unknown.NAME))) {
            throw new TypeError("Invalid comparison expression");
        }
        return new BooleanValue(true);
    }

    @Override
    public Value visit(Scope ctx, FunctionCall fc) {
        Scope funcScope = fc.scope().newChildScope();
        String name = fc.identifier();
        if (!ctx.hasVar(name)) {
            throw new FunctionException("Called function \"" + name + "\" is not defined");
        }
        for (Map.Entry<String, Expression> entry : fc.args().entrySet()) {
            funcScope.setLocalVar(entry.getKey(), entry.getValue().accept(ctx, this));
        }
        return ctx.getVar(name).asFunction().accept(funcScope, this);
    }

    @Override
    public Value visit(Scope ctx, VariableExpression ve) {
        String name = ve.identifier();
        if (!ctx.hasVar(name)) {
            throw new VariableException("Variable " + name + " is undefined");
        }
        return ctx.getVar(name);
    }

    @Override
    public Value visit(Scope ctx, Value v) {
        return v;
    }

    @Override
    public Value visit(Scope ctx, AbstractFunction af) {
        return af.checkArgs(ctx);
    }

    @Override
    public Value visit(Scope ctx, Function f) {
        for (Statement s : f.getStatements()) {
            s.accept(ctx, this);
        }
        return new Unknown();
    }

    @Override
    public Value visit(Scope ctx, Program program) {
        for (Statement s : program.statements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, FunctionDefinition fd) {
        String name = fd.name();

        if (ctx.hasVar(name)) {
            throw new FunctionException("declared function " + name + " already exists");
        }
        ctx.setVar(fd.name(), new Function(fd.statements(), fd.params()));

        Scope childScope = ctx.newChildScope();
        for (Map.Entry<String, String> entry : fd.params().entrySet()) {
            childScope.setVar(entry.getKey(), new Unknown());
        }
        for (Statement s : fd.statements()) {
            s.accept(childScope, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, IfStatement is) {
        for (Statement s : is.statements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, LoopStatement ls) {
        Value value = ls.array().accept(ctx, this);
        if (!Objects.equals(value.getTypeName(), Array.NAME)) throw new FunctionException("Cannot loop over non-array value"); // TODO - new exception
        Array array = value.asArray();
        Scope loopScope = ctx.newChildScope();
        loopScope.setLocalVar(ls.loopVar(), array.get().size() > 0 ? array.get().get(0) : new Unknown());
        for (Statement s : ls.statements()) {
            s.accept(loopScope, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, Return r) {
        return r.e().accept(ctx, this);
    }

    @Override
    public Value visit(Scope ctx, VariableAssignment va) {
        if (ctx.hasVar(va.dest())) {
            Value prevVal = ctx.getVar(va.dest());
            if (Objects.equals(prevVal.getTypeName(), AbstractFunction.NAME)) throw new FunctionException("Cannot redefine function: " + va.dest());
        }
        ctx.setVar(va.dest(), va.expr().accept(ctx, this));
        return null;
    }

    @Override
    public Value visit(Scope ctx, ExpressionWrapper ew) {
        ew.e().accept(ctx, this);
        return null;
    }
}
