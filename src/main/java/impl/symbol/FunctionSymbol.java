package impl.symbol;
import impl.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// A function is both a Symbol and the scope of its parameters/top-level locals.
public final class FunctionSymbol extends Scope implements Symbol {
    private final String name;
    private final Type returnType;
    private final List<VariableSymbol> parameters = new ArrayList<>();

    public FunctionSymbol(String name, Type returnType, Scope parent) {
        super(parent);
        this.name = name;
        this.returnType = returnType;
    }

    @Override public String getName() { return name; }
    @Override public Type getType() { return returnType; }
    public List<VariableSymbol> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    // TODO: define in this function scope, then append to the ordered list.
    public boolean addParameter(VariableSymbol parameter) {
        throw new UnsupportedOperationException("TODO: FunctionSymbol.addParameter");
    }
}
