package parser;

import core.Scope;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.exceptions.DSLParserError;

public class GifParserTest {

    @Test
    public void testBadArithmeticOperation() {
        String input = """
            SET x - "hi" AS var
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow string in arithmetic operations");
        } catch (DSLParserError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(8, e.getColumnPosition());
        }
    }

    @Test
    public void testBadComparison() {
        String input = """
            IF (x > "hi"):
              PRINT "hello"
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow string in comparisons");
        } catch (DSLParserError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(8, e.getColumnPosition());
        }
    }

    private Pair<Program, Scope> compile(String input) {
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        return compiler.compile(CharStreams.fromString(input));
    }
}
