package impl;

import framework.project4.ErrorReporter;
import generated.Splc;
import generated.SplcBaseVisitor;
import impl.symbol.*;
import impl.type.*;

// Project 3 name-resolution implementation, extended with Project 4 type checking.
public final class SemanticAnalyzer extends SplcBaseVisitor<Void> {
    private final GlobalScope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;
    private final ErrorReporter reporter;

    public SemanticAnalyzer(ErrorReporter reporter) { this.reporter = reporter; }
    public GlobalScope getGlobalScope() { return globalScope; }

    public void analyze(Splc.ProgramContext program) {
        // TODO: visit global definitions in source order.
        visit(program);
    }

    private Type resolveType(Splc.TypeContext ctx) {
        // Project 3: INT -> PrimitiveType.INT; STRUCT -> previously completed
        // canonical StructType from GlobalScope.
        throw new UnsupportedOperationException("TODO: resolveType");
    }

    private Type resolveDeclaredType(Splc.VariableDeclContext ctx) {
        // Project 3: resolve the base type and wrap array dimensions right-to-left.
        throw new UnsupportedOperationException("TODO: resolveDeclaredType");
    }

    private void enterScope() { currentScope = new Scope(currentScope); }
    private void exitScope() { currentScope = currentScope.getParent(); }

    @Override
    public Void visitStructDef(Splc.StructDefContext ctx) {
        // Project 3: build members in declaration order; register only a
        // complete, internally valid StructType.
        return null;
    }

    @Override
    public Void visitGlobalVarDef(Splc.GlobalVarDefContext ctx) {
        // Project 3: resolve the declared type and define the global variable.
        return null;
    }

    @Override
    public Void visitFunctionDef(Splc.FunctionDefContext ctx) {
        // Project 3: register the function before its body (direct recursion).
        // Use FunctionSymbol as the function scope; the outermost body shares it.
        return null;
    }

    @Override
    public Void visitBlock(Splc.BlockContext ctx) {
        // Project 3: nested blocks create a child scope and restore it on exit.
        return null;
    }

    @Override
    public Void visitLocalVarDef(Splc.LocalVarDefContext ctx) {
        // Project 3: define the variable before visiting its initializer.
        // Project 4: check initializer compatibility; array initialization is not supported.
        return null;
    }

    @Override
    public Void visitReturnStatement(Splc.ReturnStatementContext ctx) {
        // TODO: check the expression against the current function return type.
        return null;
    }

    @Override
    public Void visitIfStatement(Splc.IfStatementContext ctx) {
        // TODO: check the condition type; visit both blocks.
        return null;
    }

    @Override
    public Void visitWhileStatement(Splc.WhileStatementContext ctx) {
        // TODO: check the condition type; visit the loop block.
        return null;
    }

    @Override
    public Void visitExpression(Splc.ExpressionContext ctx) {
        // TODO: retain Project 3 name resolution and add Project 4 type checking.
        // You may choose any suitable internal implementation.
        return null;
    }
}
