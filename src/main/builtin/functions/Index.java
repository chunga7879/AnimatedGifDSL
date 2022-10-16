package builtin.functions;

import core.Scope;
import core.exceptions.InvalidArgumentException;
import core.values.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Index extends AbstractFunction {
    public final static String ACTUAL_NAME = "Index";

    @Override
    public Value call(Scope scope) {
        ArrayList<Value> array = scope.getLocalVar("a").asArray().get();
        int size = array.size();
        int index = scope.getLocalVar("i").asInteger().get();
        if (index >= size) {
            throw new InvalidArgumentException("provided index is out of bounds");
        }
        return array.get(index);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Value checkReturn() {
        return new Image(null);
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("a", Array.NAME);
            put("i", IntegerValue.NAME);
        }};
    }
}
