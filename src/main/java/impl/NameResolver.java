package impl;

import framework.project3.ErrorReporter;
import generated.Splc;
import generated.SplcBaseVisitor;
import impl.symbol.*;
import impl.type.*;
import org.antlr.v4.runtime.Token;
import java.util.List;

// Builds declarations in source order; no expression type checking is done here.
public final class NameResolver extends SplcBaseVisitor<Void> {
    private final GlobalScope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;
    private final ErrorReporter reporter;
    public NameResolver(ErrorReporter reporter) { this.reporter = reporter; }
    public GlobalScope getGlobalScope() { return globalScope; }

    public void analyze(Splc.ProgramContext program) {
        // The generated visitor walks globalDef children in source order.
        visit(program);
    }

    private Type resolveType(Splc.TypeContext ctx) {
        if (ctx.INT() != null) return PrimitiveType.INT;
        Token name = ctx.Identifier().getSymbol();
        StructType type = globalScope.resolveStruct(name.getText());
        if (type == null) reporter.undefinedStruct(name);
        return type;
    }

    private Type resolveDeclaredType(Splc.VariableDeclContext ctx) {
        Type type = resolveType(ctx.type());
        if (type == null) return null;
        List<Splc.ArrayDimensionContext> dimensions = ctx.arrayDimension();
        // The rightmost dimension is the innermost array.
        for (int i = dimensions.size() - 1; i >= 0; i--) {
            int length = Integer.parseInt(dimensions.get(i).Number().getText());
            type = new ArrayType(type, length);
        }
        return type;
    }

    private void enterScope() { currentScope = new Scope(currentScope); }
    private void exitScope() { currentScope = currentScope.getParent(); }

    @Override public Void visitStructDef(Splc.StructDefContext ctx) {
        Token name = ctx.Identifier().getSymbol();
        boolean valid = globalScope.resolveStruct(name.getText()) == null;
        if (!valid) reporter.redefinedStruct(name);
        StructType structure = new StructType(name.getText());
        for (Splc.FieldDefContext field : ctx.fieldDef()) {
            Splc.VariableDeclContext declaration = field.variableDecl();
            Type type = resolveDeclaredType(declaration);
            if (type == null) {
                valid = false;
                continue;
            }
            VariableSymbol member = new VariableSymbol(declaration.Identifier().getText(), type);
            if (!structure.defineMember(member)) {
                reporter.duplicateMember(declaration.Identifier().getSymbol());
                valid = false;
            }
        }
        // Self/forward references cannot see this unfinished structure.
        if (valid) globalScope.defineStruct(structure);
        return null;
    }

    @Override public Void visitGlobalVarDef(Splc.GlobalVarDefContext ctx) {
        Splc.VariableDeclContext declaration = ctx.variableDecl();
        Type type = resolveDeclaredType(declaration);
        if (type != null && !globalScope.define(
                new VariableSymbol(declaration.Identifier().getText(), type))) {
            reporter.redefinedSymbol(declaration.Identifier().getSymbol());
        }
        return null;
    }

    @Override public Void visitFunctionDef(Splc.FunctionDefContext ctx) {
        Type returnType = resolveType(ctx.type());
        FunctionSymbol function = new FunctionSymbol(ctx.Identifier().getText(), returnType, globalScope);
        // Register before the body to permit recursion. Invalid return types and
        // duplicates use an unregistered temporary scope, preserving the original.
        if (returnType != null && !globalScope.define(function)) {
            reporter.redefinedSymbol(ctx.Identifier().getSymbol());
        }
        Scope enclosingScope = currentScope;
        currentScope = function;
        try {
            if (ctx.parameterList() != null) {
                for (Splc.ParameterContext parameter : ctx.parameterList().parameter()) {
                    Splc.VariableDeclContext declaration = parameter.variableDecl();
                    Type type = resolveDeclaredType(declaration);
                    if (type != null && !function.addParameter(
                            new VariableSymbol(declaration.Identifier().getText(), type))) {
                        reporter.redefinedSymbol(declaration.Identifier().getSymbol());
                    }
                }
            }
            // Parameters and the outermost body share the function scope.
            for (Splc.StatementContext statement : ctx.block().statement()) visit(statement);
        } finally {
            currentScope = enclosingScope;
        }
        return null;
    }

    @Override public Void visitBlock(Splc.BlockContext ctx) {
        enterScope();
        try {
            for (Splc.StatementContext statement : ctx.statement()) visit(statement);
        } finally {
            exitScope();
        }
        return null;
    }

    @Override public Void visitLocalVarDef(Splc.LocalVarDefContext ctx) {
        Splc.VariableDeclContext declaration = ctx.variableDecl();
        Type type = resolveDeclaredType(declaration);
        if (type != null && !currentScope.define(
                new VariableSymbol(declaration.Identifier().getText(), type))) {
            reporter.redefinedSymbol(declaration.Identifier().getSymbol());
        }
        // A successfully defined variable is visible in its own initializer.
        // Even when the declaration failed, resolve all initializer names.
        if (ctx.expression() != null) visit(ctx.expression());
        return null;
    }

    @Override public Void visitExpression(Splc.ExpressionContext ctx) {
        if (ctx.Identifier() != null && ctx.DOT() == null) {
            Token name = ctx.Identifier().getSymbol();
            if (currentScope.resolve(name.getText()) == null) reporter.undeclaredIdentifier(name);
        }
        // Visits operands, receiver, indices and arguments. Terminal field names
        // after DOT are never treated as ordinary symbol references.
        return visitChildren(ctx);
    }
}
