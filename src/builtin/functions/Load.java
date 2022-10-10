package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InvalidFilePath;
import core.expressions.ExpressionVisitor;
import core.values.*;
import files.filesystem.FileSystem;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Load extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        // Documentation for LOAD doesn't have filepath being passed with the WITH keyword so
        // assumed that it's $target
        StringValue filePath = scope.getVar("$target").asString();
        try {
            ImmutableImage image = FileSystem.openImage(filePath.get());
            return new Image(image);
        } catch (FileNotFoundException e) {
            throw new InvalidFilePath(filePath + "could not be found.");
        }
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("$target", StringValue.NAME);
        }};
        checker(scope, params);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
