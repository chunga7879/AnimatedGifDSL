package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InternalException;
import core.exceptions.InvalidFilePath;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Null;
import core.values.Value;
import files.gif.GifMaker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Save extends AbstractFunction {

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
