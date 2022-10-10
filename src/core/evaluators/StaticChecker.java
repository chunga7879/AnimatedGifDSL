package core.evaluators;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import core.NodeVisitor;
import core.Scope;
import core.exceptions.FunctionException;
import core.expressions.*;
import core.statements.*;
import core.values.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StaticChecker implements NodeVisitor<Scope, Void>, ExpressionVisitor<Scope, Void>,
    StatementVisitor<Scope, Void> {
    private final Map<String, Boolean> functions = new HashMap<>() {{
        put("ADD", true);
        put("CREATE-LIST", true);
        put("LOAD", true);
        put("PRINT", true);
        put("RANDOM", true);
        put("SAVE", true);
    }};

    @Override
    public Void visit(Scope ctx, ArithmeticExpression ae) {
//        if (ae.getClass().getName() != IntegerValue.NAME || ae.getClass().getName() != IntegerValue.NAME) {
//            // throw error;
//        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, ComparisonExpression ce) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, FunctionCall fc) {
        Scope funcScope = ctx.newChildScope();
        String name = fc.getName();
        if (!functions.containsKey(name)) {
            throw new FunctionException(name + " is not defined");
        }
        for (Map.Entry<String, Expression> entry : fc.getArgs().entrySet()) {
            //funcScope.setVar(entry.getKey(), entry.getValue().accept(ctx, this));
        }
        ctx.getVar(fc.getName()).asFunction().accept(funcScope, this);
        return null;
    }

    @Override
    public Void visit(Scope ctx, VariableExpression ve) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, Value v) {
        return null;
    }

    @Override
    public Void visit(Scope ctx, AbstractFunction af) {
        af.checkArgs(ctx);
        return null;
    }

    @Override
    public Void visit(Scope ctx, Function f) {
        for (Statement s : f.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, FunctionDefinition fd) {
        String name = fd.getName();

        if (functions.containsKey(name)) {
            throw new FunctionException(name + " already exists");
        } else {
            functions.put(name, true);
        }

        for (Statement s : fd.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, IfStatement is) {
        for (Statement s : is.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, LoopStatement ls) {
        // use regex to check if string is a number?
        ls.getArray().accept(ctx, this);
        for (Statement s : ls.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Void visit(Scope ctx, Return r) {
        r.accept(ctx, this);
        return null;
    }

    @Override
    public Void visit(Scope ctx, VariableAssignment va) {
        va.accept(ctx, this);
        return null;
    }
}
