package core.statements;

import core.Scope;
import core.values.Function;

import java.util.List;

public class FunctionDefinition implements Statement{
    private final List<Statement> statements;
    private final String name;

    public FunctionDefinition(String name, List<Statement> statements) {
        this.statements = statements;
        this.name = name;
    }

    @Override
    public void Do(Scope s) {
        s.setVar(this.name, new Function(this.statements));
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return null;
    }
}
