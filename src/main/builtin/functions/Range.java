package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.IntegerValue;
import core.values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Range extends AbstractFunction {
    private final static String ACTUAL_NAME = "Range";

    @Override
    public Value call(Scope scope) {
        List<Value> nums = new ArrayList<>();
        for (int i = scope.getLocalVar("start").asInteger().get(); i < scope.getLocalVar("end").asInteger().get(); i++) {
            nums.add(new IntegerValue(i));
        }
        return new Array(nums);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Value checkReturn() {
        return new Array();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("start", IntegerValue.NAME);
            put("end", IntegerValue.NAME);
        }};
    }
}
