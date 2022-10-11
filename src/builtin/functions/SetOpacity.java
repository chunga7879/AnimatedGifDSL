package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class SetOpacity extends AbstractFunction {
    public final static String ACTUAL_NAME = "Set-Opacity";
    @Override
    public Image call(Scope scope) {
        ImmutableImage image = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        IntegerValue opacityAmount = scope.getVar("amount").asInteger();

        if (opacityAmount.get() > 100 || opacityAmount.get() < 0) {
            throw new IllegalArgumentException("Opacity value should be within [0, 100]");
        }

        // scrimage library accepts alpha range from 0 - 255
        int scaledOpacity = (int) (opacityAmount.get() * 2.55);

        ImmutableImage transparentImage = image.map((p) -> {
                if (p.alpha() != 0) {
                    return new RGBColor(p.red(), p.green(), p.blue(), scaledOpacity).awt();
                }
                return p.toColor().awt();
        });

        return new Image(transparentImage);
    }

    @Override
    public Image checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
            put("amount", IntegerValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new Image(null);
    }
}
