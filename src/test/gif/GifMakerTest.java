package test.gif;

import com.sksamuel.scrimage.ImmutableImage;
import org.junit.jupiter.api.Test;
import gif.GifMaker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GifMakerTest {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    @Test
    public void testMakeGif1Fps() throws Exception {
        List<ImmutableImage> frames = new ArrayList<>();

        frames.add(ImmutableImage.filled(WIDTH, HEIGHT, Color.RED));
        frames.add(ImmutableImage.filled(WIDTH, HEIGHT, Color.ORANGE));
        frames.add(ImmutableImage.filled(WIDTH, HEIGHT, Color.YELLOW));
        frames.add(ImmutableImage.filled(WIDTH, HEIGHT, Color.GREEN));
        frames.add(ImmutableImage.filled(WIDTH, HEIGHT, Color.BLUE));

        GifMaker.makeGif(frames, 30L, "src/test/gif/testMakeGif1Fps.gif");
    }
}
