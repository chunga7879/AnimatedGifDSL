package parser;

import core.expressions.*;
import core.expressions.arithmetic.*;
import core.expressions.comparison.*;
import core.statements.*;
import core.values.Colour;
import core.values.Function;
import core.values.IntegerValue;
import core.values.StringValue;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

import static parser.GifDSLParser.*;

public class GifDSLConverter {
    private static class DSLParserException extends RuntimeException {
        public DSLParserException(String message) {
            super(message);
        }
    }

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

    public Function convertProgram(ProgramContext programCtx) {
        return new Function(convertStatements(programCtx.statement()));
    }

    private ArrayList<Statement> convertStatements(List<StatementContext> statementCtxs) {
        Queue<StatementContext> statementCtxQueue = new LinkedList<>(statementCtxs);
        ArrayList<Statement> statements = new ArrayList<>();
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

    public Statement convertStatement(StatementContext statementCtx, List<StatementContext> innerStatementCtxs) {
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

    public Statement convertFunction(FunctionContext functionCtx, List<With_statementContext> withCtx) {
        String functionName = functionCtx.FUNCTION_NAME().getText().toLowerCase();
        HashMap<String, Expression> args = new HashMap<>();
        String returnVariable = null;
        if (functionCtx.function_as() != null) {
            returnVariable = getVariableName(functionCtx.function_as().VARIABLE());
        }
        if (functionCtx.function_on() != null) {
            VariableExpression on = convertVariable(functionCtx.function_on().VARIABLE());
            args.put(Function.PARAM_ON, on);
        }
        if (functionCtx.function_target() != null) {
            Expression target = convertExpression(functionCtx.function_target().expression());
            args.put(Function.PARAM_TARGET, target);
        }
        // TODO: add shortcut for as
        for (With_statementContext withStatementContext : withCtx) {
            String parameterName = getVariableName(withStatementContext.VARIABLE());
            Expression inputValue = convertExpression(withStatementContext.expression());
            args.put(parameterName, inputValue);
        }
        if (returnVariable == null) {
            return new FunctionCall(functionName, args);
        } else {
            return new VariableAssignment(returnVariable, new FunctionCall(functionName, args));
        }
    }

    public Statement convertControl(ControlContext controlCtx, List<StatementContext> innerStatementCtxs) {
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

    public VariableAssignment convertDefine(Define_statementContext defineCtx, List<StatementContext> innerStatementCtxs) {
        String functionName = getVariableName(defineCtx.define_function().VARIABLE());
        ArrayList<Statement> statements = convertStatements(innerStatementCtxs);
        String target = null;
        HashSet<String> parameters = new HashSet<>();
        if (defineCtx.define_target() != null) {
            target = getVariableName(defineCtx.define_target().VARIABLE());
        }
        if (defineCtx.define_params() != null) {
            List<TerminalNode> parameterNodes = defineCtx.define_params().VARIABLE();
            for (TerminalNode parameterNode : parameterNodes) {
                parameters.add(getVariableName(parameterNode));
            }
        }
        // Add target and parameters to function
        Function function = new Function(statements);
        return new VariableAssignment(functionName, function);
    }

    public static ComparisonVisitor convertComparator(String string) {
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

    public IfStatement convertIf(If_statementContext ifCtx, List<StatementContext> innerStatementCtxs) {
        ComparisonContext comparisonCtx = ifCtx.comparison();
        ComparisonVisitor comparator =  convertComparator(comparisonCtx.COMPARE().getText());
        Expression expression1 = convertArithmetic(comparisonCtx.arithmetic(0));
        Expression expression2 = convertArithmetic(comparisonCtx.arithmetic(1));
        List<Statement> innerStatements = convertStatements(innerStatementCtxs);
        return new IfStatement(new ComparisonExpression(expression1, expression2, comparator), innerStatements);
    }

    public LoopStatement convertLoop(Loop_statementContext loopCtx, List<StatementContext> innerStatementCtxs) {
        return null;
    }

    public Return convertReturn(Return_statementContext ctx) {
        return new Return(convertExpression(ctx.expression()));
    }

    public Expression convertExpression(ExpressionContext expressionContext) {
        if (expressionContext.arithmetic() != null) {
            return convertArithmetic(expressionContext.arithmetic());
        } else if (expressionContext.TEXT() != null) {
            return new StringValue(expressionContext.TEXT().getText());
        } else if (expressionContext.TEXT() != null) {
            return new Colour(expressionContext.COLOUR().getText());
        }
        throw new DSLParserException("Invalid expression");
    }

    public static ArithmeticVisitor convertArithmeticOperator(String string) {
        return switch (string) {
            case "+" -> new AdditionVisitor(string);
            case "-" -> new SubtractionVisitor(string);
            case "*" -> new MultiplicationVisitor(string);
            case "/" -> new DivisionVisitor(string);
            default -> throw new DSLParserException("Invalid operator");
        };
    }

    public Expression convertArithmetic(ArithmeticContext arithmeticContext) {
        if (arithmeticContext.num_or_var().size() == 2) {
            ArithmeticVisitor operator = convertArithmeticOperator(arithmeticContext.OPERATOR().getText());
            return new ArithmeticExpression(
                convertNumOrVar(arithmeticContext.num_or_var(0)),
                convertNumOrVar(arithmeticContext.num_or_var(1)),
                operator
            );
        } else if (arithmeticContext.num_or_var().size() == 1) {
            return convertNumOrVar(arithmeticContext.num_or_var(0));
        } else {
            throw new DSLParserException("invalid arithmetic");
        }
    }

    public Expression convertNumOrVar(Num_or_varContext numOrVarCtx) {
        if (numOrVarCtx.NUMBER() != null) {
            return new IntegerValue(Integer.parseInt(numOrVarCtx.NUMBER().getText()));
        } else if (numOrVarCtx.VARIABLE() != null) {
            return new VariableExpression(numOrVarCtx.getText());
        } else {
            throw new DSLParserException("Invalid number or variable");
        }
    }

    public VariableExpression convertVariable(TerminalNode variableNode) {
        return new VariableExpression(getVariableName(variableNode));
    }

    private static String getVariableName(TerminalNode variableNode) {
        return variableNode.getText().toLowerCase();
    }
}
