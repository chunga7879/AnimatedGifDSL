package test.checker;

import core.Scope;
import core.evaluators.StaticChecker;
import core.exceptions.DSLException;
import core.expressions.FunctionCall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TestStaticChecker {

    private StaticChecker staticChecker;
    private Scope scope;

    @BeforeEach
    public void runBefore() {
        staticChecker = new StaticChecker();
        scope = new Scope();
    }

    @Test
    public void testCallUndefinedFunction() {
        try {
            staticChecker.visit(scope, new FunctionCall("foo", new HashMap<>(), scope));
            fail("should have thrown an exception");
        } catch (DSLException e) {
            // expected
        }
    }
}
