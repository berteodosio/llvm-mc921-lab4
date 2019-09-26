import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyVisitor extends SimpleMathBaseVisitor<Integer> {

    private final Set<String> declaredVariableIdentifiers = new HashSet<>();
    private final Set<DeclaredFunction> declaredFunctionIdentifiers = new HashSet<>();

    private final CompilationLog compilationLog = new CompilationLog();

    private final CodeGenerator codeGenerator = new CodeGenerator();

    private DeclaredFunction currentDeclaredFunction = null;

    String getCompilationLog() {
        return compilationLog.getFullLog();
    }

    String getGeneratedCode() {
        return codeGenerator.getFullGeneratedCode();
    }

    private void eraseCurrentDeclaredFunction() {
        this.currentDeclaredFunction = null;
    }

    @Override
    public Integer visitSSemicolonS(final SimpleMathParser.SSemicolonSContext ctx) {
        eraseCurrentDeclaredFunction();
        return super.visitSSemicolonS(ctx);
    }

    @Override
    public Integer visitSEOF(final SimpleMathParser.SEOFContext ctx) {
        eraseCurrentDeclaredFunction();
        return super.visitSEOF(ctx);
    }

    @Override
    public Integer visitSVarDeclaration(final SimpleMathParser.SVarDeclarationContext ctx) {
        eraseCurrentDeclaredFunction();
        final String variableName = ctx.var_declaration().ID().getText();
        final boolean symbolNotDeclaredPreviously = assertSymbolNotDeclared(variableName);

        if (symbolNotDeclaredPreviously) {
            declaredVariableIdentifiers.add(variableName);
        }

        codeGenerator.generateGlobalVar(ctx);
        return super.visitSVarDeclaration(ctx);
    }

    @Override
    public Integer visitFunc_param_list(final SimpleMathParser.Func_param_listContext ctx) {
        codeGenerator.generateFunctionParameterList(ctx);
        return super.visitFunc_param_list(ctx);
    }

    @Override
    public Integer visitFunc_body(final SimpleMathParser.Func_bodyContext ctx) {
        codeGenerator.generateFunctionBody(ctx);
        return super.visitFunc_body(ctx);
    }

    @Override
    public Integer visitSFuncDeclaration(final SimpleMathParser.SFuncDeclarationContext ctx) {
        final String functionName = ctx.func_declaration().ID().getText();
        assertSymbolNotDeclared(functionName);
        final DeclaredFunction declaredFunction = new DeclaredFunction(functionName, getDeclaredFunctionArgumentList(ctx));
        this.currentDeclaredFunction = declaredFunction;
        declaredFunctionIdentifiers.add(declaredFunction);

        codeGenerator.generateFunctionHeader(ctx);
        return super.visitSFuncDeclaration(ctx);
    }

    private List<String> getDeclaredFunctionArgumentList(final SimpleMathParser.SFuncDeclarationContext ctx) {
        return ctx.func_declaration().func_param_list().ID()
                .stream()
                .map(ParseTree::getText)
                .collect(Collectors.toList());
    }

    @Override
    public Integer visitFunc_invocation(final SimpleMathParser.Func_invocationContext ctx) {
        final String invokedFunctionName = ctx.ID().getText();
        final int invokedFunctionArgumentsCount = ctx.func_invocation_param_list().func_invocation_param().size();
        assertFunctionDeclaredBeforeInvocation(invokedFunctionName);
        assertInvokedFunctionArgumentsCountMatchDeclaredFunction(invokedFunctionName, invokedFunctionArgumentsCount);
        return super.visitFunc_invocation(ctx);
    }

    private void assertInvokedFunctionArgumentsCountMatchDeclaredFunction(final String invokedFunctionName,
                                                                          final int invokedFunctionArgumentsCount) {
        final DeclaredFunction declaredFunction = declaredFunctionIdentifiers
                .stream()
                .filter((DeclaredFunction d) -> d.name.equals(invokedFunctionName))
                .findFirst()
                .orElse(null);

        if (declaredFunction == null) {
            return;
        }

        if (declaredFunction.argumentCount != invokedFunctionArgumentsCount) {
            compilationLog.addBadArgumentCount(invokedFunctionName);
        }
    }

    @Override
    public Integer visitDeclared_id(final SimpleMathParser.Declared_idContext ctx) {
        final String declaredIdentifier = ctx.ID().getText();
        assertDeclaredIdentifier(declaredIdentifier);
        return super.visitDeclared_id(ctx);
    }

    private boolean declaredFunctionSetContainsId(final String id) {
        return declaredFunctionIdentifiers
                .stream()
                .anyMatch((DeclaredFunction d) -> d.name.equals(id));
    }

    private void assertDeclaredIdentifier(final String declaredIdentifier) {
        if (!declaredVariableIdentifiers.contains(declaredIdentifier)
                && !declaredFunctionSetContainsId(declaredIdentifier)
                && !isCurrentFunctionArgument(declaredIdentifier)) {

            compilationLog.addSymbolUndeclaredMessage(declaredIdentifier);
            return;
        }

        // allows already declared function names to be overwritten with parameter names
        if (declaredFunctionSetContainsId(declaredIdentifier) && !isCurrentFunctionArgument(declaredIdentifier)) {
            compilationLog.addBadUsedSymbolMessage(declaredIdentifier);
        }
    }

    private boolean isCurrentFunctionArgument(final String identifier) {
        return currentDeclaredFunction != null
                && currentDeclaredFunction.argumentNames
                .stream()
                .anyMatch((it) -> it.equals(identifier));
    }

    private void assertFunctionDeclaredBeforeInvocation(final String invokedFunctionName) {
        if (!declaredFunctionSetContainsId(invokedFunctionName) && !declaredVariableIdentifiers.contains(invokedFunctionName)) {
            compilationLog.addSymbolUndeclaredMessage(invokedFunctionName);
            return;
        }

        if (declaredVariableIdentifiers.contains(invokedFunctionName)) {
            compilationLog.addBadUsedSymbolMessage(invokedFunctionName);
        }
    }

    /**
     * Checks if a symbol have been declared previously, either as a global var a global func
     * and adds an error to the compilation log if the symbol have already been
     * declared previously.
     *
     * @param symbol the symbol name to be checked
     * @return true if symbol have not been declared previously and false otherwise
     */
    private boolean assertSymbolNotDeclared(final String symbol) {
        if (symbolAlreadyDeclared(symbol)) {
            compilationLog.addSymbolAlreadyDeclaredMessage(symbol);
            return false;
        }

        return true;
    }

    private boolean symbolAlreadyDeclared(final String variableName) {
        return declaredVariableIdentifiers.contains(variableName) || declaredFunctionSetContainsId(variableName);
    }

    private static class DeclaredFunction {

        private final String name;
        private final int argumentCount;
        private final List<String> argumentNames;

        private DeclaredFunction(final String name, final List<String> argumentNames) {
            this.name = name;
            this.argumentCount = argumentNames.size();
            this.argumentNames = argumentNames;
        }

    }

    private static class CompilationLog {

        private String compilationLogMessage = "";

        private void addMessage(final CompilerErrorMessage compilerErrorMessage, final String symbol) {
            compilationLogMessage += compilerErrorMessage.getMessage(symbol) + "\n";
        }

        void addSymbolAlreadyDeclaredMessage(final String symbol) {
            addMessage(CompilerErrorMessage.SYMBOL_ALREADY_DECLARED, symbol);
        }

        void addSymbolUndeclaredMessage(final String symbol) {
            addMessage(CompilerErrorMessage.SYMBOL_UNDECLARED, symbol);
        }

        void addBadUsedSymbolMessage(final String invokedFunctionName) {
            addMessage(CompilerErrorMessage.BAD_USED_SYMBOL, invokedFunctionName);
        }

        void addBadArgumentCount(final String invokedFunctionName) {
            addMessage(CompilerErrorMessage.BAD_ARGUMENT_COUNT, invokedFunctionName);
        }

        String getFullLog() {
            return compilationLogMessage;
        }

        private enum CompilerErrorMessage {
            SYMBOL_UNDECLARED("Symbol undeclared: "),
            BAD_USED_SYMBOL("Bad used symbol: "),
            SYMBOL_ALREADY_DECLARED("Symbol already declared: "),
            BAD_ARGUMENT_COUNT("Bad argument count: "),
            ;

            final String message;

            CompilerErrorMessage(final String s) {
                this.message = s;
            }

            public String getMessage(final String symbolName) {
                return message + symbolName;
            }
        }

    }
}
