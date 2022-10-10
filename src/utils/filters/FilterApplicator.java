package utils.filters;

import com.sksamuel.scrimage.ImmutableImage;
import core.exceptions.InternalException;
import com.sksamuel.scrimage.filter.*;

import java.io.IOException;

public class FilterApplicator {

    private static ImmutableImage filter(ImmutableImage image, com.sksamuel.scrimage.filter.Filter filter) {
        try {
            ImmutableImage filtered = image.filter(filter);
            return filtered;
        } catch (IOException ioException) {
            throw new InternalException("Error occurred when filtering image.");
        }
    }

    public static ImmutableImage invert(ImmutableImage image) {
        return filter(image, new InvertFilter());
    }

    public static ImmutableImage greyScale(ImmutableImage image) {
        return filter(image, new GrayscaleFilter());
    }

    public static ImmutableImage retro(ImmutableImage image) {
        return filter(image, new OldPhotoFilter());
    }

    public static ImmutableImage sepia(ImmutableImage image) {
        return filter(image, new SepiaFilter());
    }

    public static ImmutableImage sharpen(ImmutableImage image) {
        return filter(image, new SharpenFilter());
    }

    public static ImmutableImage blur(ImmutableImage image) {
        return filter(image, new BlurFilter());
    }
}
