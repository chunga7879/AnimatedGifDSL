package misc;

import builtin.functions.Concat;
import core.Scope;
import core.values.Array;
import core.values.IntegerValue;
import core.values.StringValue;
import core.values.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcatTest {
    @Test
    public void testConcatStringInt() {
        Scope s = new Scope();
        s.setVar("v", new Array(new ArrayList<>(){
            {
                add(new StringValue("test"));
                add(new IntegerValue(1));
            }
        }));

        Concat f = new Concat();
        Value ret = f.call(s);
        assertTrue(ret.asString().get().equals("test1"));
    }

}
