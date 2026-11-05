package impl.symbol;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    protected final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private final Scope parent;

    public Scope(Scope parent) { this.parent = parent; }
    public Scope getParent() { return parent; }
    public Collection<Symbol> getLocalSymbols() {
        return Collections.unmodifiableCollection(symbols.values());
    }

    // TODO: check only this scope; shadowing in child scopes is allowed.
    public boolean define(Symbol symbol) {
        throw new UnsupportedOperationException("TODO: Scope.define");
    }

    // TODO: look up only in this scope.
    public Symbol resolveLocal(String name) {
        throw new UnsupportedOperationException("TODO: Scope.resolveLocal");
    }

    // TODO: search this scope, then parent scopes.
    public Symbol resolve(String name) {
        throw new UnsupportedOperationException("TODO: Scope.resolve");
    }
}
