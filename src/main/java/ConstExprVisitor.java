import framework.project2.ConstExprResult;
import generated.SplcBaseVisitor;
import generated.SplcParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
public class ConstExprVisitor extends SplcBaseVisitor<Integer> {
    private final List<ConstExprResult> results = new ArrayList<>();
    public final List<ConstExprResult> getResults() {
        return Collections.unmodifiableList(results);
    }
    protected final void addResult(String expression, int value) {
        results.add(new ConstExprResult(expression, value));
    }
    /**
     * Every local variable definition with an initializer is a candidate: the
     * complete right-hand side expression of the optional ASSIGN is examined.
     *
     * <pre>
     * localVarDef
     *     : variableDecl (ASSIGN expression)? SEMI
     *     ;
     * </pre>
     *
     * A result is recorded only when that complete expression is a constexpr.
     * Constexpr subexpressions of a non-constexpr initializer are deliberately
     * not reported, and plain assignment statements are never visited here.
     */
    @Override
    public Integer visitLocalVarDef(SplcParser.LocalVarDefContext ctx) {
        SplcParser.ExpressionContext initializer = ctx.expression();
        if (initializer != null) {
            Integer value = evaluate(initializer);
            if (value != null) {
                addResult(initializer.getText(), value.intValue());
            }
        }
        // Keep walking the rest of the tree: a localVarDef may appear inside
        // arbitrarily nested blocks, so each one must still be inspected.
        return visitChildren(ctx);
    }
    /**
     * Recursively evaluates one expression node.
     *
     * @return the value when the node is a constexpr, or {@code null} when the
     *         node is not a constexpr according to the Project 2 definition.
     */
    private Integer evaluate(ParseTree node) {
        if (!(node instanceof SplcParser.ExpressionContext)) {
            return null;
        }
        SplcParser.ExpressionContext ctx = (SplcParser.ExpressionContext) node;
        switch (ctx.getChildCount()) {
            case 1:
                // Number      -> integer constant, a constexpr
                // Identifier  -> variable, not a constexpr
                return evaluateIntegerConstant(ctx.getChild(0));
            case 2: {
                // PLUS expression | MINUS expression | NOT expression
                int op = tokenType(ctx.getChild(0));
                if (op == SplcParser.PLUS || op == SplcParser.MINUS) {
                    Integer operand = evaluate(ctx.getChild(1));
                    if (operand == null) {
                        return null;
                    }
                    return op == SplcParser.MINUS
                            ? Integer.valueOf(-operand.intValue())
                            : operand;
                }
                // Logical NOT is not a constexpr.
                return null;
            }
            case 3: {
                // LPAREN expression RPAREN -> parenthesized expression
                if (isToken(ctx.getChild(0), SplcParser.LPAREN)
                        && isToken(ctx.getChild(2), SplcParser.RPAREN)) {
                    return evaluate(ctx.getChild(1));
                }
                // expression OP expression -> binary expression
                int op = arithmeticOperator(ctx.getChild(1));
                if (op < 0) {
                    // POW, comparisons, equality, AND/OR, ASSIGN, DOT and
                    // "Identifier LPAREN RPAREN" are not constexprs.
                    return null;
                }
                Integer left = evaluate(ctx.getChild(0));
                if (left == null) {
                    return null;
                }
                Integer right = evaluate(ctx.getChild(2));
                if (right == null) {
                    return null;
                }
                return apply(op, left.intValue(), right.intValue());
            }
            default:
                // 4 children: function call with an argument list, array access.
                // Neither is a constexpr.
                return null;
        }
    }
    /** Parses a {@code Number} token; anything else is not a constexpr. */
    private Integer evaluateIntegerConstant(ParseTree node) {
        if (tokenType(node) != SplcParser.Number) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(node.getText()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    /** Applies one supported binary operator to two constexpr operands. */
    private Integer apply(int op, int left, int right) {
        switch (op) {
            case SplcParser.PLUS:
                return Integer.valueOf(left + right);
            case SplcParser.MINUS:
                return Integer.valueOf(left - right);
            case SplcParser.STAR:
                return Integer.valueOf(left * right);
            case SplcParser.DIV:
                return right == 0 ? null : Integer.valueOf(left / right);
            case SplcParser.MOD:
                return right == 0 ? null : Integer.valueOf(left % right);
            default:
                return null;
        }
    }
    /**
     * Returns the token type of the arithmetic operator at {@code node}, or -1
     * when the node is not one of the operators that may build a constexpr.
     */
    private int arithmeticOperator(ParseTree node) {
        switch (tokenType(node)) {
            case SplcParser.PLUS:
            case SplcParser.MINUS:
            case SplcParser.STAR:
            case SplcParser.DIV:
            case SplcParser.MOD:
                return tokenType(node);
            default:
                return -1;
        }
    }
    private static int tokenType(ParseTree node) {
        if (node instanceof TerminalNode) {
            return ((TerminalNode) node).getSymbol().getType();
        }
        return -1;
    }
    private static boolean isToken(ParseTree node, int type) {
        return tokenType(node) == type;
    }
}