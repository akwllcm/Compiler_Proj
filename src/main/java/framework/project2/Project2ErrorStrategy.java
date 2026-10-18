package framework.project2;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 * Project 2 error strategy.
 *
 * ANTLR's {@link DefaultErrorStrategy} keeps full responsibility for the
 * recovery algorithm itself: single-token insertion, single-token deletion,
 * {@code recoverInline()} and {@code sync()} are not reimplemented here. Only
 * the two reports are adapted to {@link ErrorReporter} so that they follow the
 * Project 2 rules:
 *
 * <ul>
 *   <li>the reported token type is the symbolic ANTLR token type;</li>
 *   <li>lines are 0-indexed, while {@link Token#getLine()} is 1-indexed.</li>
 * </ul>
 */
public final class Project2ErrorStrategy extends DefaultErrorStrategy {

    private final ErrorReporter reporter;

    public Project2ErrorStrategy(ErrorReporter reporter) {
        this.reporter = reporter;
    }

    /**
     * Missing Symbol.
     *
     * The missing token is the token the parser expects at its current
     * position. The reported line is the line of the lookahead token that has
     * not been consumed yet: that is the token the recovery is still sitting on
     * when the absence is detected.
     */
    @Override
    protected void reportMissingToken(Parser recognizer) {
        if (inErrorRecoveryMode(recognizer)) {
            return;
        }

        beginErrorCondition(recognizer);

        int missingTokenType = expectedTokenType(recognizer);
        if (missingTokenType == Token.INVALID_TYPE) {
            return;
        }

        reporter.reportMissing(missingTokenType, zeroBasedLine(recognizer.getCurrentToken()));
    }

    /**
     * Extraneous Symbol.
     *
     * The extraneous token is the current lookahead token, which is the token
     * single-token deletion is about to discard. It is reported with its own
     * line.
     */
    @Override
    protected void reportUnwantedToken(Parser recognizer) {
        if (inErrorRecoveryMode(recognizer)) {
            return;
        }

        beginErrorCondition(recognizer);

        Token offending = recognizer.getCurrentToken();
        if (offending == null) {
            return;
        }

        reporter.reportExtraneous(offending.getType(), zeroBasedLine(offending));
    }

    /**
     * Token type the parser expects at its current state.
     *
     * Project 2 only exercises recoverable single-token insertions, where the
     * pending set holds exactly one token; the smallest element of the set is
     * that token.
     */
    private static int expectedTokenType(Parser recognizer) {
        IntervalSet expected = recognizer.getExpectedTokens();
        if (expected == null || expected.size() == 0) {
            return Token.INVALID_TYPE;
        }
        return expected.getMinElement();
    }

    /** ANTLR counts lines from 1, Project 2 counts them from 0. */
    private static int zeroBasedLine(Token token) {
        if (token == null) {
            return 0;
        }
        return Math.max(0, token.getLine() - 1);
    }
}
