package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.exceptions.InvalidFilePath;
import core.values.*;
import files.filesystem.FileSystem;

import java.io.FileNotFoundException;

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
}
