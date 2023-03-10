package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class GetHeight extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-Height";
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();

        return new IntegerValue(immutableImg.height);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
        }};
    }

    @Override
    public IntegerValue checkReturn() {
        return new IntegerValue(0);
    }
}
