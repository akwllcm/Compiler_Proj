package impl.symbol;
import impl.type.StructType;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GlobalScope extends Scope {
    private final Map<String, StructType> structTypes = new LinkedHashMap<>();

    public GlobalScope() { super(null); }
    public Collection<StructType> getStructTypes() {
        return Collections.unmodifiableCollection(structTypes.values());
    }

    // TODO: register a completed struct type; reject a duplicate name.
    public boolean defineStruct(StructType type) {
        throw new UnsupportedOperationException("TODO: GlobalScope.defineStruct");
    }

    // TODO: search only the global struct-type table.
    public StructType resolveStruct(String name) {
        throw new UnsupportedOperationException("TODO: GlobalScope.resolveStruct");
    }
}
