package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.IntegerValue;
import core.values.Value;

public class GetWidth extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();

        return new IntegerValue(immutableImg.width);
    }
}
