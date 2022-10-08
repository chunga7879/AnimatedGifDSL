package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Null;
import core.values.StringValue;
import core.values.Value;

import java.util.ArrayList;

public class Load extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        // Documentation for LOAD doesn't have filepath being passed with the WITH keyword so
        // assumed that it's $target
        StringValue filePath = scope.getVar("$target").asString();

        return new Null();
    }

//    StringValue filePath = scope.getVar("$target").asArray());
//    //TODO: Uncomment after NumberValue is added to core
//    //Long duration = Long.valueOf(scope.getVar("duration").asNumber.get());
//    String location = scope.getVar("location").asString().get();
//    //GifMaker.makeGif(frames, duration, location);
//
}
