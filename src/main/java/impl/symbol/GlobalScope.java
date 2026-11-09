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
    // register only completely defined structs; same name => false.
    public boolean defineStruct(StructType type) {
        if (structTypes.containsKey(type.getName())) return false;
        structTypes.put(type.getName(), type);
        return true;
    }
    // global type table ONLY (not Scope.resolve).
    public StructType resolveStruct(String name) {
        return structTypes.get(name);
    }
}
