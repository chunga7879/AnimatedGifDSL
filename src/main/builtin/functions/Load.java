package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InvalidFilePath;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.StringValue;
import files.filesystem.FileSystem;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Load extends AbstractFunction {
    public final static String ACTUAL_NAME = "Load";

    @Override
    public Image call(Scope scope) {
        // Documentation for LOAD doesn't have filepath being passed with the WITH keyword so
        // assumed that it's $target
        StringValue filePath = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asString();
        try {
            ImmutableImage image = FileSystem.openImage(filePath.get());
            if (image == null) throw new InvalidFilePath("File could not be loaded.");
            return new Image(image);
        } catch (FileNotFoundException e) {
            throw new InvalidFilePath(filePath.get() + " could not be found.");
        }
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, StringValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }
}
