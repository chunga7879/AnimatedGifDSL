package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;
import utils.filters.FilterApplicator;

import java.util.HashMap;
import java.util.Map;

public class Filter extends AbstractFunction {
    public static String ACTUAL_NAME = "Filter";

    @Override
    public Value call(Scope scope) {
        ImmutableImage image = scope.getVar("$target").asImage().get();
        String filter = scope.getVar("filter").asString().get();

        ImmutableImage filteredImage = performFilter(filter, image);
        return new Image(filteredImage);
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("$target", Image.NAME);
            put("filter", StringValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
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

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
