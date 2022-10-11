package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.IntegerValue;
import core.values.Value;

public class GetHeight extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();

        return new IntegerValue(immutableImg.height);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
