package impl.type;
import impl.symbol.VariableSymbol;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StructType implements Type {
    private final String name;
    private final Map<String, VariableSymbol> members = new LinkedHashMap<>();
    public StructType(String name) { this.name = name; }
    public String getName() { return name; }
    // return false on same-struct duplicate; preserve declaration order.
    public boolean defineMember(VariableSymbol member) {
        if (members.containsKey(member.getName())) return false;
        members.put(member.getName(), member);
        return true;
    }
    // look up ONLY within this struct, never in lexical parent scopes.
    public VariableSymbol resolveMember(String name) {
        return members.get(name);
    }
    public Collection<VariableSymbol> getMembers() {
        return Collections.unmodifiableCollection(members.values());
    }
    @Override public String prettyPrint() { return "struct " + name; }
}
