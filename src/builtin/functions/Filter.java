package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;
import utils.filters.FilterApplicator;

public class Filter extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        ImmutableImage image = scope.getVar("$target").asImage().get();
        String filter = scope.getVar("filtering").asString().get();

        ImmutableImage filteredImage = performFilter(filter, image);
        return new Image(filteredImage);
    }

    private ImmutableImage performFilter(String filter, ImmutableImage image) {
        ImmutableImage filteredImage;
        switch (filter) {
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
