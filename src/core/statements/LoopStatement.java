package core.statements;

import core.Scope;
import core.expressions.Expression;
import core.values.Array;
import core.values.Value;

import java.util.List;

public class LoopStatement implements Statement {
    private final Expression array;
    private final List<Statement> statements;
    private final String loopVar;

    public LoopStatement(Expression array, List<Statement> statements, String loopVar) {
        this.array = array;
        this.statements = statements;
        this.loopVar = loopVar;
    }

    @Override
    public void Do(Scope s) {
        Array a = this.array.evaluate(s).asArray();
        for (Value v : a) {
            Scope loopScope = s.newChildScope();
            loopScope.setVar(this.loopVar, v);
            for (Statement stms : this.statements) {
                stms.Do(s);
            }
        }
    }
}
