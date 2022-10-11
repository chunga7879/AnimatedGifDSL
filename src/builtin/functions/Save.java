package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InternalException;
import core.exceptions.InvalidFilePath;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import files.gif.GifMaker;
import core.values.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Save extends AbstractFunction {
    public final static String ACTUAL_NAME = "Save";

    @Override
    public Value call(Scope scope) {
        ArrayList<ImmutableImage> frames = getImmutableImages(scope.getVar("$target").asArray());
        long duration = scope.getVar("duration").asInteger().get();
        String location = scope.getVar("location").asString().get();

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
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("duration", IntegerValue.NAME);
            put("location", StringValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
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

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
