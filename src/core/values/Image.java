package core.values;

import com.sksamuel.scrimage.ImmutableImage;

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
}
