import core.Scope;
import core.evaluators.Evaluator;
import core.exceptions.InvalidFilePath;
import core.statements.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import parser.GifDSLCompiler;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new IOException("Requires input file as arg");
            }
            GifDSLCompiler compiler = new GifDSLCompiler();
            HashSet<String> parameters = new HashSet<>(List.of(args).subList(1, args.length));
            if (parameters.contains("-nocheck") && parameters.contains("-onlycheck")) {
                throw new IOException("Cannot have arguments \"-nocheck\" and \"-onlycheck\" at the same time");
            }
            if (parameters.contains("-nocheck")) {
                compiler.setEnableStaticChecker(false);
            }
            if (parameters.contains("-shortcuts")) {
                compiler.setEnableShortcuts(true);
            }

            CharStream charStream;
            try {
                charStream = CharStreams.fromFileName(args[0]);
            } catch (NoSuchFileException e) {
                throw new InvalidFilePath("Input file \"" + e.getMessage() + "\" could not be found.");
            }
            Pair<Program, Scope> main = compiler.compile(charStream);

            // Quit early for -onlycheck
            if (parameters.contains("-onlycheck")) return;

            System.out.println("[Gif DSL Runner] Starting runner");
            Evaluator evaluator = new Evaluator();
            evaluator.visit(main.b, main.a);
            System.out.println("[Gif DSL Runner] Finished runner");
        } catch (Exception e) {
            System.err.println(e.getMessage());
//            throw e;
        }
    }
}
