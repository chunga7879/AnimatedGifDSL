package core.evaluators;

import core.Scope;
import core.exceptions.InternalException;
import core.expressions.*;
import core.statements.*;
import core.values.*;

import java.util.Map;

public class Evaluator implements StatementVisitor<Scope, Void>, ExpressionVisitor<Scope, Value> {
    @Override
    public Void visit(Scope ctx, Program program) {
        for (Statement s : program.statements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, FunctionDefinition fd) {
        ctx.setVar(fd.name(), new Function(fd.statements(), fd.params()));
        return null;
    }

    @Override
    public Void visit(Scope ctx, IfStatement ifs) {
        if (!ifs.cond().accept(ctx, this).asBoolean().get())
            return null;

        // TODO do we want to support early returns?
        for (Statement s : ifs.statements()) {
            s.accept(ctx, this);
        }

        return null;
    }

    @Override
    public Void visit(Scope ctx, LoopStatement ls) {
        Array a = ls.array().accept(ctx, this).asArray();
        Scope loopScope = ctx.newChildScope();
        for (Value v : a) {
            loopScope.setLocalVar(ls.loopVar(), v);
            for (Statement stms : ls.statements()) {
                stms.accept(loopScope, this);
            }
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, Return r) {
        throw new InternalException("visitReturn should never be called");
    }

    @Override
    public Void visit(Scope ctx, VariableAssignment va) {
        if (va.local()) {
            ctx.setLocalVar(va.dest(), va.expr().accept(ctx, this));
        } else {
            ctx.setVar(va.dest(), va.expr().accept(ctx, this));
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, ArithmeticExpression ae) {
        return ae.a().accept(ctx, this).accept(ae.av(), ae.b().accept(ctx, this), ctx);
    }

    @Override
    public Value visit(Scope ctx, ComparisonExpression ce) {
        return ce.a().accept(ctx, this).accept(ce.cv(), ce.b().accept(ctx, this), ctx);
    }

    @Override
    public Value visit(Scope ctx, FunctionCall fc) {
        Scope funcScope = fc.scope().newChildScope();
        for (Map.Entry<String, Expression> entry : fc.args().entrySet()) {
            funcScope.setLocalVar(entry.getKey(), entry.getValue().accept(ctx, this));
        }
        return ctx.getVar(fc.identifier()).asFunction().accept(funcScope, this);
    }

    @Override
    public Void visit(Scope ctx, ExpressionWrapper ew) {
        ew.e().accept(ctx, this);
        return null;
    }

    @Override
    public Value visit(Scope ctx, VariableExpression ve) {
        return ctx.getVar(ve.identifier());
    }

    @Override
    public Value visit(Scope ctx, Value v) {
        return v;
    }

    @Override
    public Value visit(Scope ctx, AbstractFunction f) {
        return f.call(ctx);
    }

    @Override
    public Value visit(Scope ctx, Function f) {
        for (Statement s : f.getStatements()) {
            if (s instanceof Return) {
                return ((Return) s).e().accept(ctx, this);
            } else {
                s.accept(ctx, this);
            }
        }

        return Null.NULL;
    }
}
