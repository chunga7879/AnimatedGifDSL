package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.exceptions.FunctionException;
import core.exceptions.InternalException;
import core.exceptions.InvalidFilePath;
import core.values.*;
import files.gif.GifMaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Save extends AbstractFunction {
    public final static String ACTUAL_NAME = "Save";

    @Override
    public Null call(Scope scope) {
        ArrayList<ImmutableImage> frames = getImmutableImages(scope.getVar(AbstractFunction.PARAM_TARGET).asArray());
        long duration = scope.getVar("duration").asInteger().get();
        String location = scope.getVar("location").asString().get();

        if (frames.size() < 1) throw new FunctionException("Attempting to save with no image");
        try {
            GifMaker.makeGif(frames, duration, location);
        } catch (IOException ioe) {
            throw new InvalidFilePath(location + " is invalid");
        } catch (Exception e) {
            throw new InternalException("something went wrong: " + e.getMessage());
        }

        return Null.NULL;
    }

    @Override
    public Null checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Array.NAME);
            put("duration", IntegerValue.NAME);
            put("location", StringValue.NAME);
        }};
    }

    @Override
    public Null checkReturn() {
        return Null.NULL;
    }

    private ArrayList<ImmutableImage> getImmutableImages(Array array) {
        ArrayList<ImmutableImage> immutableImgs = new ArrayList<>();
        ArrayList<Value> imgs = array.get();

        for (Value value : imgs) {
            ImmutableImage immutableImg = value.asImage().get();
            immutableImgs.add(immutableImg);
        }

        return immutableImgs;
    }
}
