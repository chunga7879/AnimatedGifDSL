package core.values;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.expressions.comparison.ComparisonVisitor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Unknown type, introduced to help static checking
 */
public class Unknown extends Value {
    public static final String NAME = "unknown";

    public Unknown() {
        super(NAME);
    }

    @Override
    public StringValue asString() {
        return new StringValue("");
    }

    @Override
    public AbstractFunction asFunction() {
        return new Function(new ArrayList<>(), new HashMap<>());
    }

    @Override
    public Array asArray() {
        return new Array();
    }

    @Override
    public Colour asColour() {
        return new Colour(0, 0, 0);
    }

    @Override
    public IntegerValue asInteger() {
        return new IntegerValue(0);
    }

    @Override
    public BooleanValue asBoolean() {
        return new BooleanValue(false);
    }

    @Override
    public Image asImage() {
        return new Image(ImmutableImage.create(0, 0));
    }

    @Override
    public BooleanValue accept(ComparisonVisitor cv, Value b, Scope s) {
        return cv.visit(this.asString(), b, s);
    }

    @Override
    public Value accept(ArithmeticVisitor av, Value b, Scope s) {
        return av.visit(this.asInteger(), b, s);
    }

    @Override
    public String getTypeName() {
        return super.getTypeName();
    }
}
