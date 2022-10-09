package core.evaluators;

import builtin.functions.*;
import core.NodeVisitor;
import core.Scope;
import core.expressions.*;
import core.statements.*;
import core.values.*;

public class Evaluator implements NodeVisitor<Scope, Function>, ExpressionVisitor<Scope, Expression>,
    StatementVisitor<Scope, Statement> {
    @Override
    public Expression visit(Scope ctx, ArithmeticExpression ae) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, ComparisonExpression ce) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, FunctionCall fc) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, VariableExpression ve) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Array a) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, BooleanValue bv) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Colour c) {
        return null;
    }

    @Override
    public Function visit(Scope ctx, Function f) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Image i) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, IntegerValue iv) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Null n) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, StringValue sv) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Add a) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, CreateList cl) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Load l) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Print p) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Random r) {
        return null;
    }

    @Override
    public Expression visit(Scope ctx, Save s) {
        return null;
    }

    @Override
    public Statement visit(Scope ctx, FunctionDefinition fd) {
        return null;
    }

    @Override
    public Statement visit(Scope ctx, IfStatement is) {
        return null;
    }

    @Override
    public Statement visit(Scope ctx, LoopStatement ls) {
        return null;
    }

    @Override
    public Statement visit(Scope ctx, Return r) {
        return null;
    }

    @Override
    public Statement visit(Scope ctx, VariableAssignment va) {
        return null;
    }
}
