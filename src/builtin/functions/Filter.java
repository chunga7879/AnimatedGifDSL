package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.StringValue;
import core.values.Value;
import utils.filters.FilterApplicator;

import java.util.HashMap;
import java.util.Map;

public class Filter extends AbstractFunction {
    public static String ACTUAL_NAME = "Filter";

    @Override
    public Value call(Scope scope) {
        ImmutableImage image = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        String filter = scope.getVar("filtering").asString().get();

        ImmutableImage filteredImage = performFilter(filter, image);
        return new Image(filteredImage);
    }

    @Override
    public Image checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
            put("filtering", StringValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }

    private ImmutableImage performFilter(String filter, ImmutableImage image) {
        ImmutableImage filteredImage;
        switch (filter.toLowerCase()) {
            case "sepia":
                filteredImage = FilterApplicator.sepia(image);
                break;
            case "blur":
                filteredImage = FilterApplicator.blur(image);
                break;
            case "sharpen":
                filteredImage = FilterApplicator.sharpen(image);
                break;
            case "greyscale":
                filteredImage = FilterApplicator.greyScale(image);
                break;
            case "invert":
                filteredImage = FilterApplicator.invert(image);
                break;
            case "chrome":
                filteredImage = FilterApplicator.chrome(image);
                break;
            default:
                throw new IllegalArgumentException("Filter not supported.");
        }
        return filteredImage;
    }
}
