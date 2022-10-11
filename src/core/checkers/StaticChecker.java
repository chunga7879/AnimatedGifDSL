package core.checkers;

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

import java.util.Map;

public class StaticChecker implements NodeVisitor<Scope, Value>, ExpressionVisitor<Scope, Value>,
    StatementVisitor<Scope, Value> {
    Scope functions = new Scope();

    public StaticChecker() {
        addBuiltInFunctions();
    }

    @Override
    public Value visit(Scope ctx, ArithmeticExpression ae) {
//        if (ae.getClass().getName() != IntegerValue.NAME || ae.getClass().getName() != IntegerValue.NAME) {
//            // throw error;
//        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, ComparisonExpression ce) {
        //return ce
        return null;
    }

    @Override
    public Value visit(Scope ctx, FunctionCall fc) {
        Scope funcScope = ctx.newChildScope();
        String name = fc.getName();
        if (!functions.hasVar(name)) {
            throw new FunctionException("called function " + name + " is not defined");
        }
        for (Map.Entry<String, Expression> entry : fc.getArgs().entrySet()) {
            funcScope.setVar(entry.getKey(), entry.getValue().accept(ctx, this));
        }
        functions.getVar(fc.getName()).asFunction().accept(funcScope, this);
        return null;
    }

    @Override
    public Value visit(Scope ctx, VariableExpression ve) {
        //return ctx.getVar(ve)
        return null;
    }

    @Override
    public Value visit(Scope ctx, Value v) {
        return v;
    }

    @Override
    public Value visit(Scope ctx, AbstractFunction af) {
        af.checkArgs(ctx);
        return null;
    }

    @Override
    public Value visit(Scope ctx, Function f) {
        for (Statement s : f.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, FunctionDefinition fd) {
        String name = fd.getName();

        if (functions.hasVar(name)) {
            throw new FunctionException("declared function " + name + " already exists");
        } else {
            functions.setVar(fd.getName(), new Function(fd.getStatements()));
        }

        for (Statement s : fd.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, IfStatement is) {
        for (Statement s : is.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, LoopStatement ls) {
        ls.getArray().accept(ctx, this);
        for (Statement s : ls.getStatements()) {
            s.accept(ctx, this);
        }
        return null;
    }

    @Override
    public Value visit(Scope ctx, Return r) {
        r.accept(ctx, this);
        return null;
    }

    @Override
    public Value visit(Scope ctx, VariableAssignment va) {
        ctx.setVar(va.getDest(), va.getExpr().accept(ctx, this));
        return null;
    }

    private void addBuiltInFunctions() {
        functions.setVar(CreateColour.ACTUAL_NAME, new CreateColour());
        functions.setVar(GetB.ACTUAL_NAME, new GetB());
        functions.setVar(GetG.ACTUAL_NAME, new GetG());
        functions.setVar(GetR.ACTUAL_NAME, new GetR());
        functions.setVar(Add.ACTUAL_NAME, new Add());
        functions.setVar(ColourFill.ACTUAL_NAME, new ColourFill());
        functions.setVar(CreateList.ACTUAL_NAME, new CreateList());
        functions.setVar(CreateRectangle.ACTUAL_NAME, new CreateRectangle());
        functions.setVar(Crop.ACTUAL_NAME, new Crop());
        functions.setVar(Filter.ACTUAL_NAME, new Filter());
        functions.setVar(GetHeight.ACTUAL_NAME, new GetHeight());
        functions.setVar(GetWidth.ACTUAL_NAME, new GetWidth());
        functions.setVar(Load.ACTUAL_NAME, new Load());
        functions.setVar(Overlay.ACTUAL_NAME, new Overlay());
        functions.setVar(Print.ACTUAL_NAME, new Print());
        functions.setVar(Random.ACTUAL_NAME, new Random());
        functions.setVar(Resize.ACTUAL_NAME, new Resize());
        functions.setVar(Rotate.ACTUAL_NAME, new Rotate());
        functions.setVar(Save.ACTUAL_NAME, new Save());
        functions.setVar(SetOpacity.ACTUAL_NAME, new SetOpacity());
        functions.setVar(Translate.ACTUAL_NAME, new Translate());
        functions.setVar(Write.ACTUAL_NAME, new Write());
    }
}
