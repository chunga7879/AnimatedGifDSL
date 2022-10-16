package misc;

import builtin.functions.Range;
import core.Scope;
import core.values.IntegerValue;
import core.values.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RangeTest {
    @Test
    public void testRange() {
        final int start = 0;
        final int end = 10;
        Range f = new Range();

        Scope s = new Scope();
        s.setVar("start", new IntegerValue(start));
        s.setVar("end", new IntegerValue(end));
        Value v = f.call(s);
        ArrayList<Value> a = v.asArray().get();
        assertEquals(a.size(), end);
        for (int i = start; i < end; i++) {
            assertEquals(a.get(i).asInteger().get(), i);
        }
    }
}
