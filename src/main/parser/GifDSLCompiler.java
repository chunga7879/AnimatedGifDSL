package parser;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import core.values.AbstractFunction;
import utils.ColourConstant;

import java.util.Arrays;
import java.util.List;

public class GifDSLCompiler extends DSLCompiler {
    public GifDSLCompiler() {
        super();
        // Add built-in functions
        List<AbstractFunction> builtInFunctions = Arrays.asList(
            new Set(),
            new CreateColour(),
            new GetB(),
            new GetG(),
            new GetR(),
            new Add(),
            new ColourFill(),
            new CreateList(),
            new CreateRectangle(),
            new Crop(),
            new Filter(),
            new GetHeight(),
            new GetWidth(),
            new Load(),
            new Overlay(),
            new Print(),
            new Random(),
            new Resize(),
            new Rotate(),
            new Save(),
            new SetOpacity(),
            new Translate(),
            new Write()
        );
        for (AbstractFunction function : builtInFunctions) {
            addPredefinedValues(function.getFunctionName(), function);
        }
        // Add constants
        for (ColourConstant c: ColourConstant.values()) {
            addConstantValues(c.getName(), c.createColour());
        }
    }
}
