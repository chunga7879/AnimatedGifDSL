package core.checkers;

import core.Scope;
import core.exceptions.*;
import core.expressions.*;
import core.statements.*;
import core.values.*;

import java.util.List;
import java.util.Map;

public class StaticChecker implements ExpressionVisitor<Scope, Value>, StatementVisitor<Scope, Void> {
    private List<String> constants;

    public StaticChecker(List<String> constants) {
        this.constants = constants;
    }

    @Override
    public Value visit(Scope ctx, ArithmeticExpression ae) {
        Value valueA = ae.a().accept(ctx, this);
        Value valueB = ae.b().accept(ctx, this);
        if (!TypeChecker.checkValueIsTypeOrUnknown(valueA, IntegerValue.NAME) ||
            !TypeChecker.checkValueIsTypeOrUnknown(valueB, IntegerValue.NAME)) {
            throw new TypeError("Invalid arithmetic expression").withPosition(ae);
        }
        return new IntegerValue(0);
    }

    @Override
    public Value visit(Scope ctx, ComparisonExpression ce) {
        Value valueA = ce.a().accept(ctx, this);
        Value valueB = ce.b().accept(ctx, this);
        if (!TypeChecker.checkValueIsTypeOrUnknown(valueA, IntegerValue.NAME) ||
            !TypeChecker.checkValueIsTypeOrUnknown(valueB, IntegerValue.NAME)) {
            throw new TypeError("Invalid comparison expression").withPosition(ce);
        }
        return new BooleanValue(true);
    }

    @Override
    public Value visit(Scope ctx, FunctionCall fc) {
        Scope funcScope = ctx.getGlobalScope().newChildScope();
        String name = fc.identifier();
        if (!ctx.hasVar(name)) {
            throw new FunctionNameException("Called function \"" + name + "\" is not defined").withPosition(fc);
        }
        for (Map.Entry<String, Expression> entry : fc.args().entrySet()) {
            funcScope.setLocalVar(entry.getKey(), entry.getValue().accept(ctx, this));
        }
        try {
            return ctx.getVar(name).asFunction().accept(funcScope, this);
        } catch (DSLException e) {
            throw e.withPosition(fc);
        }
    }

    @Override
    public Value visit(Scope ctx, VariableExpression ve) {
        String name = ve.identifier();
        if (!ctx.hasVar(name)) {
            throw new NameError(name).withPosition(ve);
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
        f.checkArgs(ctx);
        Value returnValue = Null.NULL;
        for (Statement s : f.getStatements()) {
            if (!(returnValue instanceof Null)) {
                throw new StatementException("Cannot have statements after Return").withPosition(s);
            }
            if (s instanceof Return r) {
                returnValue = r.e().accept(ctx, this);
            } else {
                s.accept(ctx, this);
            }
        }
        return returnValue;
    }

    @Override
    public Void visit(Scope ctx, Program program) {
        for (Statement s : program.statements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, FunctionDefinition fd) {
        String name = fd.name();

        if (ctx.hasVar(name)) {
            throw new FunctionNameException("Declared function \"" + name + "\" already exists").withPosition(fd);
        }

        for (Statement statement : fd.statements()) {
            failIfFunctionDefinition(statement);
        }

        Function function = new Function(fd.statements(), fd.params());
        ctx.setVar(fd.name(), function);

        // Check what would happen if defined function is called
        Scope childScope = ctx.getGlobalScope().newChildScope();
        for (Map.Entry<String, String> entry : fd.params().entrySet()) {
            childScope.setLocalVar(entry.getKey(), new Unknown());
        }
        try {
            function.accept(childScope, this);
        } catch (DSLException e) {
            throw e.withPosition(fd);
        }

        return null;
    }

    @Override
    public Void visit(Scope ctx, IfStatement is) {
        is.cond().accept(ctx, this);
        for (Statement s : is.statements()) {
            failIfFunctionDefinition(s);
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, LoopStatement ls) {
        Value value = ls.array().accept(ctx, this);
        if (!TypeChecker.checkValueIsTypeOrUnknown(value, Array.NAME))
            throw new TypeError("Cannot loop over non-array value").withPosition(ls.array());
        Array array = value.asArray();
        Scope loopScope = ctx.newChildScope();
        loopScope.setLocalVar(ls.loopVar(), array.get().size() > 0 ? array.get().get(0) : new Unknown());
        for (Statement s : ls.statements()) {
            failIfFunctionDefinition(s);
            s.accept(loopScope, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, Return r) {
        throw new StatementException("Return can only be inside of Define").withPosition(r);
    }

    @Override
    public Void visit(Scope ctx, VariableAssignment va) {
        String dest = va.dest();
        if (constants.contains(dest)) {
            throw new VariableException("Cannot edit constant: " + dest).withPosition(va);
        }
        if (ctx.hasVar(dest)) {
            Value prevVal = ctx.getVar(va.dest());
            if (TypeChecker.checkValueIsType(prevVal, AbstractFunction.NAME))
                throw new FunctionNameException("Cannot redefine function: " + va.dest()).withPosition(va);
        }

        Value returnValue = va.expr().accept(ctx, this);

        if (returnValue instanceof Null) throw new VariableException("Expression does not return a value").withPosition(va);

        if (va.local()) {
            ctx.setLocalVar(va.dest(), returnValue);
        } else {
            ctx.setVar(va.dest(), returnValue);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, ExpressionWrapper ew) {
        ew.e().accept(ctx, this);
        return null;
    }

    /**
     * Throw exception if statement is a FunctionDefinition
     * @param statement
     */
    private void failIfFunctionDefinition(Statement statement) {
        if (statement instanceof FunctionDefinition) {
            throw new StatementException("Can only define functions at the base level").withPosition(statement);
        }
    }
}
