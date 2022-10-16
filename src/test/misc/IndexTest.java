package misc;

import builtin.functions.Index;
import core.Scope;
import core.values.Array;
import core.values.IntegerValue;
import core.values.StringValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexTest {
    @Test
    public void testIndex() {
        StringValue s1 = new StringValue("s1");
        StringValue s2 = new StringValue("s2");
        Scope s = new Scope();
        s.setVar("a", new Array(new ArrayList<>() {
            {
                add(s1);
                add(s2);
            }
        }));

        Index f = new Index();

        s.setVar("i", new IntegerValue(0));
        assertEquals(f.call(s), s1);
        s.setVar("i", new IntegerValue(1));
        assertEquals(f.call(s), s2);
    }
}
