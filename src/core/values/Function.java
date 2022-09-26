package core.values;

import core.Scope;
import core.statements.Return;
import core.statements.Statement;

import java.util.ArrayList;

// A user defined function (or main).
public class Function extends AbstractFunction {
    private final ArrayList<Statement> statements;

    public Function(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public Value call(Scope scope) {
        for (Statement s : statements) {
            if (s instanceof Return) {
                return ((Return) s).GetReturnValue(scope);
            } else {
                s.Do(scope);
            }
        }
        return new Null();
    }
}
