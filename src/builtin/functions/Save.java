package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Save extends AbstractFunction {
    public final static String ACTUAL_NAME = "Save";

    @Override
    public Value call(Scope scope) {
        ArrayList<ImmutableImage> frames = getImmutableImages(scope.getVar("$target").asArray());
        //TODO: Uncomment after NumberValue is added to core
        //Long duration = Long.valueOf(scope.getVar("duration").asNumber.get());
        String location = scope.getVar("location").asString().get();
        //GifMaker.makeGif(frames, duration, location);

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
