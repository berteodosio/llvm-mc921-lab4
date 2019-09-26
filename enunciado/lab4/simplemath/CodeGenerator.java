import org.antlr.v4.runtime.tree.TerminalNode;

import java.text.MessageFormat;

class CodeGenerator {

    private String generatedCode = "";

    private final LlvmGenerator llvmGenerator = new LlvmGenerator();

    String getFullGeneratedCode() {
        return generatedCode;
    }

    void generateGlobalVar(final SimpleMathParser.SVarDeclarationContext ctx) {
        // TODO: parse expression
        generatedCode += llvmGenerator.generateGlobalVar(ctx.var_declaration().ID().getText(), ctx.var_declaration().expression().getText());
    }

    void generateFunctionHeader(final SimpleMathParser.SFuncDeclarationContext ctx) {
        generatedCode += llvmGenerator.generateFunctionFirstLineStart(ctx.func_declaration().ID().getText());
    }

    void generateFunctionParameterList(final SimpleMathParser.Func_param_listContext ctx) {
        for (int i = 0; i < ctx.ID().size(); i++) {
            final TerminalNode parameterName = ctx.ID(i);
            generatedCode += llvmGenerator.generateFunctionParameterName(parameterName.getText(), i);
        }

        generatedCode += llvmGenerator.generateFunctionFirstLineEnd();
    }

    void generateFunctionBody(final SimpleMathParser.Func_bodyContext ctx) {
        generatedCode += llvmGenerator.generateFunctionBody(ctx.expression().getText());
        generatedCode += llvmGenerator.generateFunctionLastLine();
    }

    class LlvmGenerator {

        String generateGlobalVar(final String identifier, final String value) {
            return MessageFormat.format("@{0} global i32 {1};\n", identifier, value);
        }

        String generateFunctionFirstLineStart(final String functionName) {
            // i32 %{1})
            return MessageFormat.format("define i32 @{0}(", functionName);
        }

        String generateFunctionParameterName(final String parameterName, final int parameterIndex) {
            String message = "";
            if (parameterIndex > 0) {
                message += ", ";
            }

            message += "i32 %{0}";
            return MessageFormat.format(message, parameterName);
        }

        String generateFunctionFirstLineEnd() {
            return ") {\n";
        }

        String generateFunctionBody(final String returnValue) {
            return MessageFormat.format("ret i32 {0}", returnValue);
        }

        String generateFunctionLastLine() {
            return "}\n";
        }
    }
}