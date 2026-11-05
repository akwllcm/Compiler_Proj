package framework.project4;

public final class Diagnostic {
    public enum Kind {
        UNDECLARED_IDENTIFIER,
        REDEFINED_SYMBOL,
        UNDEFINED_STRUCT,
        REDEFINED_STRUCT,
        DUPLICATE_MEMBER,
        NOT_VARIABLE,
        NOT_FUNCTION,
        BAD_UNARY_OPERAND,
        BAD_BINARY_OPERAND,
        EXPECTED_ARRAY,
        BAD_INDEX_TYPE,
        EXPECTED_STRUCT,
        UNKNOWN_MEMBER,
        INVALID_ASSIGNMENT_TARGET,
        TYPE_MISMATCH,
        BAD_ARGUMENT_COUNT,
        BAD_ARGUMENT_TYPE,
        BAD_CONDITION_TYPE,
        ARRAY_INITIALIZER_NOT_SUPPORTED
    }

    private final Kind kind;
    private final int line;
    private final String name;

    public Diagnostic(Kind kind, int line, String name) {
        this.kind = kind;
        this.line = line;
        this.name = name;
    }

    public Kind getKind() { return kind; }
    public int getLine() { return line; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Error " + kind.name() + " at Line " + line + ": " + name;
    }
}
