package core.evaluators;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import core.NodeVisitor;
import core.Scope;
import core.exceptions.FunctionException;
import core.expressions.*;
import core.statements.*;
import core.values.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StaticChecker implements NodeVisitor<Scope, Value>, ExpressionVisitor<Scope, Value>,
    StatementVisitor<Scope, Value> {
    private Map<String, Boolean> functionTable = new HashMap<>() {{
        put("ADD", true);
        put("CREATE-LIST", true);
        put("LOAD", true);
        put("PRINT", true);
        put("RANDOM", true);
        put("SAVE", true);
    }};

    @Override
    public Value visit(Scope ctx, ArithmeticExpression ae) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, ComparisonExpression ce) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, FunctionCall fc) {
        String name = fc.getName();
        if (!functionTable.containsKey(name)) {
            throw new FunctionException(name + " is not defined");
        }
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, VariableExpression ve) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Array a) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, BooleanValue bv) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Colour c) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Function f) {
        for (Statement s : f.getStatements()) {
            s.accept(ctx, this);
        }
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Image i) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, IntegerValue iv) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Null n) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, StringValue sv) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Add a) {
        Map<String, String> args = new HashMap<>() {{
            put("array", Array.NAME);
        }};
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, CreateList cl) {
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Load l) {
        Map<String, String> args = new HashMap<>() {{
            put("$target", StringValue.NAME);
        }};
        checkArguments(ctx, args);
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Print p) {
        Map<String, String> args = new HashMap<>() {{
            // TODO: check values
            put("msg", IntegerValue.NAME);
        }};
        checkArguments(ctx, args);
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Random r) {
        Map<String, String> args = new HashMap<>() {{
            put("min", IntegerValue.NAME);
            put("max", IntegerValue.NAME);
        }};
        checkArguments(ctx, args);
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Save s) {
        Map<String, String> args = new HashMap<>() {{
            put("duration", IntegerValue.NAME);
            put("location", StringValue.NAME);
        }};
        checkArguments(ctx, args);
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, FunctionDefinition fd) {
        String functionName = fd.getName();

        if (functionTable.containsKey(functionName)) {
            throw new FunctionException(functionName + " already exists");
        } else {
            functionTable.put(functionName, true);
        }

        for (Statement s : fd.getStatements()) {
            s.accept(ctx, this);
        }
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, IfStatement is) {
        for (Statement s : is.getStatements()) {
            s.accept(ctx, this);
        }
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, LoopStatement ls) {
        for (Statement s : ls.getStatements()) {
            s.accept(ctx, this);
        }
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, Return r) {
        r.accept(ctx, this);
        return Null.NULL;
    }

    @Override
    public Value visit(Scope ctx, VariableAssignment va) {
        va.accept(ctx, this);
//        String dest = va.getDest();
//        if (ctx.hasVar(dest)) {
//            String destType = ctx.getVar(dest).getTypeName();
//            if (destType != va.)
//        }
        return Null.NULL;
    }

    private void checkArguments(Scope ctx, Map<String, String> args) {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            String argName = arg.getKey();
            if (!ctx.hasVar(argName)) {
                throw new FunctionException("argument " + argName + " not provided");
            }

            String expectedType = arg.getValue();
            String actualType = ctx.getVar(argName).getTypeName();
            if (!Objects.equals(expectedType, actualType)) {
                throw new FunctionException("argument " + argName + " is of type " + actualType + " but expected " + expectedType);
            }
        }
    }
}
