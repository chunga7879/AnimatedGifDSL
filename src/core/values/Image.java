package core.values;

import com.sksamuel.scrimage.ImmutableImage;
import core.expressions.ExpressionVisitor;

public class Image extends Value {
    private ImmutableImage img;
    public static final String NAME = "Image";

    public Image(ImmutableImage img) {
        super(Image.NAME);
        this.img = img;
    }

    public ImmutableImage get() {
        return img;
    }

    @Override
    public Image asImage() {
        return this;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
