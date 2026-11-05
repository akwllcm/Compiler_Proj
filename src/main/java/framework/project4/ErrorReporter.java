package framework.project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.antlr.v4.runtime.Token;

public final class ErrorReporter {
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    private void report(Diagnostic.Kind kind, Token token) {
        diagnostics.add(new Diagnostic(kind, token.getLine() - 1, token.getText()));
    }

    public void undeclaredIdentifier(Token t) { report(Diagnostic.Kind.UNDECLARED_IDENTIFIER, t); }
    public void redefinedSymbol(Token t) { report(Diagnostic.Kind.REDEFINED_SYMBOL, t); }
    public void undefinedStruct(Token t) { report(Diagnostic.Kind.UNDEFINED_STRUCT, t); }
    public void redefinedStruct(Token t) { report(Diagnostic.Kind.REDEFINED_STRUCT, t); }
    public void duplicateMember(Token t) { report(Diagnostic.Kind.DUPLICATE_MEMBER, t); }

    public void notVariable(Token t) { report(Diagnostic.Kind.NOT_VARIABLE, t); }
    public void notFunction(Token t) { report(Diagnostic.Kind.NOT_FUNCTION, t); }
    public void badUnaryOperand(Token t) { report(Diagnostic.Kind.BAD_UNARY_OPERAND, t); }
    public void badBinaryOperand(Token t) { report(Diagnostic.Kind.BAD_BINARY_OPERAND, t); }
    public void expectedArray(Token t) { report(Diagnostic.Kind.EXPECTED_ARRAY, t); }
    public void badIndexType(Token t) { report(Diagnostic.Kind.BAD_INDEX_TYPE, t); }
    public void expectedStruct(Token t) { report(Diagnostic.Kind.EXPECTED_STRUCT, t); }
    public void unknownMember(Token t) { report(Diagnostic.Kind.UNKNOWN_MEMBER, t); }
    public void invalidAssignmentTarget(Token t) { report(Diagnostic.Kind.INVALID_ASSIGNMENT_TARGET, t); }
    public void typeMismatch(Token t) { report(Diagnostic.Kind.TYPE_MISMATCH, t); }
    public void badArgumentCount(Token t) { report(Diagnostic.Kind.BAD_ARGUMENT_COUNT, t); }
    public void badArgumentType(Token t) { report(Diagnostic.Kind.BAD_ARGUMENT_TYPE, t); }
    public void badConditionType(Token t) { report(Diagnostic.Kind.BAD_CONDITION_TYPE, t); }
    public void arrayInitializerNotSupported(Token t) {
        report(Diagnostic.Kind.ARRAY_INITIALIZER_NOT_SUPPORTED, t);
    }

    public boolean hasErrors() { return !diagnostics.isEmpty(); }

    public List<Diagnostic> getDiagnostics() {
        List<Diagnostic> out = new ArrayList<>(diagnostics);
        out.sort(Comparator.comparingInt(Diagnostic::getLine));
        return Collections.unmodifiableList(out);
    }
}
