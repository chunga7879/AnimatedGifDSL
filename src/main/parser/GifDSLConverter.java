package parser;

import core.expressions.*;
import core.expressions.arithmetic.*;
import core.expressions.comparison.*;
import core.statements.*;
import core.values.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

import static parser.GifDSLParser.*;

/**
 * Converter to perform AST Conversion for parsed Program
 */
public class GifDSLConverter {
    public GifDSLConverter() {}

    /**
     * Count the number of indents/spaces (tabs = 2 spaces)
     * @param statement
     * @return
     */
    private int countIndent(StatementContext statement) {
        if (statement == null || statement.INDENT() == null) return 0;
        String indentText = statement.INDENT().getText();
        int count = 0;
        for (char c : indentText.toCharArray()) {
            switch(c) {
                case ' ':
                    count++;
                case '\t':
                    count += 2;
            }
        }
        return count;
    }

    /**
     * Convert the entire program
     * @param programCtx
     * @return
     */
    public Program convertProgram(ProgramContext programCtx) {
        return new Program(convertStatements(programCtx.statement()));
    }

    /**
     * Convert a list of statements
     * @param statementCtxs
     * @return
     */
    private List<Statement> convertStatements(List<StatementContext> statementCtxs) {
        Queue<StatementContext> statementCtxQueue = new LinkedList<>(statementCtxs);
        List<Statement> statements = new ArrayList<>();
        while (statementCtxQueue.size() > 0) {
            StatementContext statement = statementCtxQueue.remove();
            if (statement.COMMENT() == null) {
                int indent = countIndent(statement);
                List<StatementContext> innerStatements = new ArrayList<>();
                while (statementCtxQueue.size() > 0 && countIndent(statementCtxQueue.peek()) > indent) {
                    innerStatements.add(statementCtxQueue.remove());
                }
                statements.add(convertStatement(statement, innerStatements));
            }
        }
        return statements;
    }

    /**
     * Convert a single statement
     * @param statementCtx
     * @param innerStatementCtxs
     * @return
     */
    private Statement convertStatement(StatementContext statementCtx, List<StatementContext> innerStatementCtxs) {
        if (statementCtx.function() != null) {
            List<With_statementContext> withStatementContexts = new ArrayList<>();
            for (StatementContext inner : innerStatementCtxs) {
                if (inner.with_statement() == null) throw new DSLParserException("Function inner statement is not a with");
                withStatementContexts.add(inner.with_statement());
            }
            return convertFunction(statementCtx.function(), withStatementContexts);
        } else if (statementCtx.control() != null) {
            return convertControl(statementCtx.control(), innerStatementCtxs);
        } else if (statementCtx.return_statement() != null) {
            return convertReturn(statementCtx.return_statement());
        }
        throw new DSLParserException("Not a function or control");
    }

    /**
     * Convert function calls
     * @param functionCtx
     * @param withCtx
     * @return
     */
    private Statement convertFunction(FunctionContext functionCtx, List<With_statementContext> withCtx) {
        String functionName = functionCtx.FUNCTION_NAME().getText().toLowerCase();
        HashMap<String, Expression> args = new HashMap<>();
        String returnVariable = null;
        if (functionCtx.function_as() != null) {
            returnVariable = getVariableName(functionCtx.function_as().VARIABLE());
        }
        if (functionCtx.function_on() != null) {
            VariableExpression on = convertVariable(functionCtx.function_on().VARIABLE());
            args.put(AbstractFunction.PARAM_ON, on);
        }
        if (functionCtx.function_target() != null) {
            Expression target = convertExpression(functionCtx.function_target().expression());
            args.put(AbstractFunction.PARAM_TARGET, target);
        }
        // TODO: add shortcut for as
        for (With_statementContext withStatementContext : withCtx) {
            String parameterName = getVariableName(withStatementContext.VARIABLE());
            Expression inputValue = convertExpression(withStatementContext.expression());
            args.put(parameterName, inputValue);
        }
        FunctionCall funcCall = withExpressionPosition(new FunctionCall(functionName, args), functionCtx);
        Statement statement = returnVariable == null ?
            new ExpressionWrapper(funcCall) :
            new VariableAssignment(returnVariable, funcCall);
        return withStatementLine(statement, functionCtx);
    }

    /**
     * Convert control statements (define, if, loop)
     * @param controlCtx
     * @param innerStatementCtxs
     * @return
     */
    private Statement convertControl(ControlContext controlCtx, List<StatementContext> innerStatementCtxs) {
        Control_typeContext controlTypeCtx = controlCtx.control_type();
        if (controlTypeCtx.define_statement() != null) {
            return convertDefine(controlTypeCtx.define_statement(), innerStatementCtxs);
        } else if (controlTypeCtx.if_statement() != null) {
            return convertIf(controlTypeCtx.if_statement(), innerStatementCtxs);
        } else if (controlTypeCtx.loop_statement() != null) {
            return convertLoop(controlTypeCtx.loop_statement(), innerStatementCtxs);
        }
        throw new DSLParserException("Not define, if, or loop");
    }

    /**
     * Convert define statement
     * @param defineCtx
     * @param innerStatementCtxs
     * @return
     */
    private FunctionDefinition convertDefine(Define_statementContext defineCtx, List<StatementContext> innerStatementCtxs) {
        String functionName = getVariableName(defineCtx.define_function().VARIABLE());
        HashMap<String, String> parameters = new HashMap<>();
        List<Statement> statements = new ArrayList<>();
        if (defineCtx.define_target() != null) {
            parameters.put(AbstractFunction.PARAM_TARGET, Unknown.NAME);
            statements.add(new VariableAssignment(getVariableName(defineCtx.define_target().VARIABLE()), new VariableExpression("$target")));
        }
        if (defineCtx.define_params() != null) {
            List<TerminalNode> parameterNodes = defineCtx.define_params().VARIABLE();
            for (TerminalNode parameterNode : parameterNodes) {
                parameters.put(getVariableName(parameterNode), Unknown.NAME);
            }
        }
        statements.addAll(convertStatements(innerStatementCtxs));
        return withStatementLine(new FunctionDefinition(functionName, statements, parameters), defineCtx);
    }

    /**
     * Convert if statement
     * @param ifCtx
     * @param innerStatementCtxs
     * @return
     */
    private IfStatement convertIf(If_statementContext ifCtx, List<StatementContext> innerStatementCtxs) {
        ComparisonContext comparisonCtx = ifCtx.comparison();
        ComparisonVisitor comparator =  convertComparator(comparisonCtx.COMPARE().getText());
        Expression expression1 = convertArithmetic(comparisonCtx.arithmetic(0));
        Expression expression2 = convertArithmetic(comparisonCtx.arithmetic(1));
        List<Statement> innerStatements = convertStatements(innerStatementCtxs);
        ComparisonExpression comparison = withExpressionPosition(
            new ComparisonExpression(expression1, expression2, comparator),
            comparisonCtx
        );
        return withStatementLine(new IfStatement(comparison, innerStatements), ifCtx);
    }

    /**
     * Convert loop statement
     * @param loopCtx
     * @param innerStatementCtxs
     * @return
     */
    private LoopStatement convertLoop(Loop_statementContext loopCtx, List<StatementContext> innerStatementCtxs) {
        Expression array;
        if (loopCtx.VARIABLE() != null) {
            // for each loop
            array = convertVariable(loopCtx.VARIABLE());
        } else {
            // for range loop
            array = convertRange(loopCtx.range());
        }
        List<Statement> innerStatements = convertStatements(innerStatementCtxs);
        return withStatementLine(
            new LoopStatement(array, loopCtx.loop_variable().VARIABLE().getText(), innerStatements),
            loopCtx
        );
    }

    private Array convertRange(RangeContext rangeCtx) {
        int min = getIntegerValue(rangeCtx.NUMBER(0));
        int max = getIntegerValue(rangeCtx.NUMBER(1));
        List<Value> array = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            array.add(new IntegerValue(i));
        }
        return withExpressionPosition(new Array(array), rangeCtx);
    }

    /**
     * Convert a return statement
     * @param ctx
     * @return
     */
    private Return convertReturn(Return_statementContext ctx) {
        return withStatementLine(new Return(convertExpression(ctx.expression())), ctx);
    }

    /**
     * Convert a value or a variable expression
     * @param expressionContext
     * @return
     */
    private Expression convertExpression(ExpressionContext expressionContext) {
        if (expressionContext.arithmetic() != null) {
            return convertArithmetic(expressionContext.arithmetic());
        } else if (expressionContext.TEXT() != null) {
            return convertString(expressionContext.TEXT());
        } else if (expressionContext.COLOUR() != null) {
            return convertColour(expressionContext.COLOUR());
        }
        throw new DSLParserException("Invalid expression");
    }

    /**
     * Convert an arithmetic expression (number, variable, or arithmetic operation)
     * @param arithmeticContext
     * @return
     */
    private Expression convertArithmetic(ArithmeticContext arithmeticContext) {
        if (arithmeticContext.num_or_var().size() == 2) {
            ArithmeticVisitor operator = convertArithmeticOperator(arithmeticContext.OPERATOR().getText());
            return withExpressionPosition(new ArithmeticExpression(
                convertNumOrVar(arithmeticContext.num_or_var(0)),
                convertNumOrVar(arithmeticContext.num_or_var(1)),
                operator
            ), arithmeticContext);
        } else if (arithmeticContext.num_or_var().size() == 1) {
            return convertNumOrVar(arithmeticContext.num_or_var(0));
        } else {
            throw new DSLParserException("Invalid arithmetic");
        }
    }

    /**
     * Convert a number or a variable expression
     * @param numOrVarCtx
     * @return
     */
    public Expression convertNumOrVar(Num_or_varContext numOrVarCtx) {
        try {
            if (numOrVarCtx.NUMBER() != null) {
                return convertInteger(numOrVarCtx.NUMBER());
            } else if (numOrVarCtx.VARIABLE() != null) {
                return convertVariable(numOrVarCtx.VARIABLE());
            }
        } catch (NumberFormatException ignored) {}
        throw new DSLParserException("Invalid number or variable");
    }

    /**
     * Convert a variable expression from a TerminalNode
     * @param variableNode
     * @return
     */
    private VariableExpression convertVariable(TerminalNode variableNode) {
        return withExpressionPosition(new VariableExpression(getVariableName(variableNode)), variableNode);
    }

    /**
     * Convert an integer value from a TerminalNode
     * @param integerNode
     * @return
     */
    private IntegerValue convertInteger(TerminalNode integerNode) {
        return withExpressionPosition(new IntegerValue(getIntegerValue(integerNode)), integerNode);
    }

    /**
     * Convert a string value from a TerminalNode
     * @param stringNode
     * @return
     */
    private StringValue convertString(TerminalNode stringNode) {
        String string = stringNode.getText();
        // to remove the quotation marks
        return withExpressionPosition(new StringValue(string.substring(1, string.length() - 1)), stringNode);
    }

    /**
     * Convert a colour from a TerminalNode
     * @param colourNode
     * @return
     */
    private Colour convertColour(TerminalNode colourNode) {
        return withExpressionPosition(new Colour(colourNode.getText()), colourNode);
    }

    /**
     * Get the name of a variable from a TerminalNode
     * @param variableNode
     * @return
     */
    private static String getVariableName(TerminalNode variableNode) {
        return variableNode.getText().toLowerCase();
    }

    /**
     * Get an integer number from a TerminalNode
     * @param integerNode
     * @return
     */
    private int getIntegerValue(TerminalNode integerNode) {
        return Integer.parseInt(integerNode.getText());
    }

    /**
     * Convert a string a comparator
     * @param string
     * @return
     */
    private static ComparisonVisitor convertComparator(String string) {
        return switch (string) {
            case "=" -> new EQVisitor();
            case "!=" -> new NEVisitor();
            case ">" -> new GTVisitor();
            case "<" -> new LTVisitor();
            case ">=" -> new GTEVisitor();
            case "<=" -> new LTEVisitor();
            default -> throw new DSLParserException("Invalid comparator");
        };
    }

    /**
     * Convert string to an arithmetic operator
     * @param string
     * @return
     */
    private static ArithmeticVisitor convertArithmeticOperator(String string) {
        return switch (string) {
            case "+" -> new AdditionVisitor();
            case "-" -> new SubtractionVisitor();
            case "*" -> new MultiplicationVisitor();
            case "/" -> new DivisionVisitor();
            default -> throw new DSLParserException("Invalid operator");
        };
    }

    /**
     * Set line position in program for statement
     * @param statement The statement
     * @param ctx The context of where the statement is
     * @return
     * @param <T>
     */
    private static <T extends Statement> T withStatementLine(T statement, ParserRuleContext ctx) {
        statement.setPosition(ctx.getStart().getLine());
        return statement;
    }

    /**
     * Set line and column position in program for expression
     * @param expression The expression
     * @param ctx The context of where the expression is
     * @return
     * @param <T>
     */
    private static <T extends Expression> T withExpressionPosition(T expression, ParserRuleContext ctx) {
        expression.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        return expression;
    }

    /**
     * Set line and column position in program for expression
     * @param expression The expression
     * @param node The TerminalNode of where the expression is
     * @return
     * @param <T>
     */
    private static <T extends Expression> T withExpressionPosition(T expression, TerminalNode node) {
        expression.setPosition(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine());
        return expression;
    }

//    /**
//     * Get line in program for context
//     * @param ctx
//     * @return
//     */
//    private static int getContextLine(ParserRuleContext ctx) {
//        return ctx.getStart().getLine();
//    }
//
//    /**
//     * Get the column position in line in program for context
//     * @param ctx
//     * @return
//     */
//    private static int getContextColumnPosition(ParserRuleContext ctx) {
//        return ctx.getStart().getCharPositionInLine();
//    }
}
