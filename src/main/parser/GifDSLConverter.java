package parser;

import core.exceptions.DSLException;
import core.expressions.*;
import core.expressions.arithmetic.*;
import core.expressions.comparison.*;
import core.statements.*;
import core.values.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.exceptions.DSLConverterError;

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
        List<StatementContext> filteredStatements = filterOutComments(programCtx.statement());
        return new Program(convertStatements(filteredStatements));
    }

    /**
     * Remove comment statements from list
     * @param statementCtxs
     * @return
     */
    public List<StatementContext> filterOutComments(List<StatementContext> statementCtxs) {
        List<StatementContext> filtered = new ArrayList<>();
        for (StatementContext statement : statementCtxs) {
            if (statement.COMMENT() == null) filtered.add(statement);
        }
        return filtered;
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
            int indent = countIndent(statement);
            List<StatementContext> innerStatements = new ArrayList<>();
            while (statementCtxQueue.size() > 0 && countIndent(statementCtxQueue.peek()) > indent) {
                innerStatements.add(statementCtxQueue.remove());
            }
            statements.add(convertStatement(statement, innerStatements));
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
                if (inner.with_statement() == null) throw withExceptionPosition(
                    new DSLConverterError("Function call has a non-with statement attached"), inner);
                withStatementContexts.add(inner.with_statement());
            }
            return convertFunction(statementCtx.function(), withStatementContexts);
        } else if (statementCtx.control() != null) {
            return convertControl(statementCtx.control(), innerStatementCtxs);
        } else if (statementCtx.return_statement() != null) {
            return convertReturn(statementCtx.return_statement());
        }
        throw withExceptionPosition(new DSLConverterError("Statement is not a function or a control"), statementCtx);
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
        throw withExceptionPosition(new DSLConverterError("Statement is not define, if, or loop"), controlCtx);
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
            VariableAssignment targetAssignment = new VariableAssignment(getVariableName(defineCtx.define_target().VARIABLE()), new VariableExpression("$target"), true);
            statements.add(withStatementLine(targetAssignment, defineCtx.define_target()));
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
        ComparisonVisitor comparator =  convertComparator(comparisonCtx.COMPARE());
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

    /**
     * Convert a range (e.g. "10 to 20")
     * @param rangeCtx
     * @return
     */
    private Array convertRange(RangeContext rangeCtx) {
        int from = getIntegerValue(rangeCtx.NUMBER(0));
        int to = getIntegerValue(rangeCtx.NUMBER(1));
        List<Value> array = new ArrayList<>();
        if (from <= to) {
            for (int i = from; i <= to; i++) {
                array.add(new IntegerValue(i));
            }
        } else {
            for (int i = from; i >= to; i--) {
                array.add(new IntegerValue(i));
            }
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
        throw withExceptionPosition(new DSLConverterError("Invalid expression"), expressionContext);
    }

    /**
     * Convert an arithmetic expression (number, variable, or arithmetic operation)
     * @param arithmeticContext
     * @return
     */
    private Expression convertArithmetic(ArithmeticContext arithmeticContext) {
        if (arithmeticContext.num_or_var().size() == 2) {
            ArithmeticVisitor operator = convertArithmeticOperator(arithmeticContext.OPERATOR());
            return withExpressionPosition(new ArithmeticExpression(
                convertNumOrVar(arithmeticContext.num_or_var(0)),
                convertNumOrVar(arithmeticContext.num_or_var(1)),
                operator
            ), arithmeticContext);
        } else if (arithmeticContext.num_or_var().size() == 1) {
            return convertNumOrVar(arithmeticContext.num_or_var(0));
        } else {
            throw withExceptionPosition(new DSLConverterError("Invalid arithmetic operation"), arithmeticContext);
        }
    }

    /**
     * Convert a number or a variable expression
     * @param numOrVarCtx
     * @return
     */
    public Expression convertNumOrVar(Num_or_varContext numOrVarCtx) {
        if (numOrVarCtx.NUMBER() != null) {
            return convertInteger(numOrVarCtx.NUMBER());
        } else if (numOrVarCtx.VARIABLE() != null) {
            return convertVariable(numOrVarCtx.VARIABLE());
        }
        throw withExceptionPosition(new DSLConverterError("Not a number or variable"), numOrVarCtx);
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
        try {
            return withExpressionPosition(new Colour(colourNode.getText()), colourNode);
        } catch (Exception e) {
            throw withExceptionPosition(new DSLConverterError(e.getMessage()), colourNode);
        }
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
        try {
            return Integer.parseInt(integerNode.getText());
        } catch (NumberFormatException e) {
            throw withExceptionPosition(new DSLConverterError("Bad number format"), integerNode);
        }
    }

    /**
     * Convert a node to a comparator
     * @param node
     * @return
     */
    private static ComparisonVisitor convertComparator(TerminalNode node) {
        return switch (node.getText()) {
            case "=" -> new EQVisitor();
            case "!=" -> new NEVisitor();
            case ">" -> new GTVisitor();
            case "<" -> new LTVisitor();
            case ">=" -> new GTEVisitor();
            case "<=" -> new LTEVisitor();
            default -> throw withExceptionPosition(new DSLConverterError("Invalid comparator"), node);
        };
    }

    /**
     * Convert a node to an arithmetic operator
     * @param node
     * @return
     */
    private static ArithmeticVisitor convertArithmeticOperator(TerminalNode node) {
        return switch (node.getText()) {
            case "+" -> new AdditionVisitor();
            case "-" -> new SubtractionVisitor();
            case "*" -> new MultiplicationVisitor();
            case "/" -> new DivisionVisitor();
            default -> throw withExceptionPosition(new DSLConverterError("Invalid arithmetic operator"), node);
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

    /**
     * Set line and column position in program for exception
     * @param exception The exception to add position to
     * @param ctx The ParserRuleContext where the exception occurred
     * @return The same exception
     */
    private static DSLException withExceptionPosition(DSLException exception, ParserRuleContext ctx) {
        return exception.withPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    /**
     * Set line and column position in program for exception
     * @param exception The exception to add position to
     * @param node The TerminalNode where the exception occurred
     * @return The same exception
     */
    private static DSLException withExceptionPosition(DSLException exception, TerminalNode node) {
        return exception.withPosition(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine());
    }

}
