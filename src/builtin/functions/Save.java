package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.*;

import java.util.ArrayList;

public class Save extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        ArrayList<ImmutableImage> frames = getImmutableImages(scope.getVar("array").asArray());
        //TODO: Uncomment after NumberValue is added to core
        //Long duration = Long.valueOf(scope.getVar("duration").asNumber.get());
        String location = scope.getVar("location").asString().get();
        //GifMaker.makeGif(frames, duration, location);

        return new Null();
    }

    private ArrayList<ImmutableImage> getImmutableImages(Array array) {
        ArrayList<ImmutableImage> immutableImgs = new ArrayList<>();
        ArrayList<Value> imgs = array.get();

        for (Value value: imgs) {
            ImmutableImage immutableImg = value.asImage().get();
            immutableImgs.add(immutableImg);
        }

        return immutableImgs;
    }
}
