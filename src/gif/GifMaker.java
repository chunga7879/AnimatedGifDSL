package gif;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.StreamingGifWriter;
import com.sksamuel.scrimage.nio.StreamingGifWriter.GifStream;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.List;

public class GifMaker {

    public static void makeGif(List<ImmutableImage> frames, long duration, String location) throws Exception {
        StreamingGifWriter writer = new StreamingGifWriter(Duration.ofSeconds(duration), false);
        GifStream gif = writer.prepareStream(location, BufferedImage.TYPE_INT_ARGB);

        for (ImmutableImage frame: frames) {
            gif.writeFrame(frame);
        }

        gif.close();
    }

    public static void makeGif2(List<ImmutableImage> frames, long duration, String location) {

    }
}
