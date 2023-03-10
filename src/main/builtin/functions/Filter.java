package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InvalidArgumentException;
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
        ImmutableImage image = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();
        String filter = scope.getLocalVar("filtering").asString().get();

        ImmutableImage filteredImage = performFilter(filter, image);
        return new Image(filteredImage);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
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
                throw new InvalidArgumentException("Filter not supported.");
        }
        return filteredImage;
    }
}
