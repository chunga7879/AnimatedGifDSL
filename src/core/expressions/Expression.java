package core.expressions;

import core.Scope;
import core.values.Value;

public interface Expression {
    Value evaluate(Scope s);
}
