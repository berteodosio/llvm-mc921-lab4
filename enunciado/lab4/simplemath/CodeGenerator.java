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

    class LlvmGenerator {

        String generateGlobalVar(final String identifier, final String value) {
            return MessageFormat.format("@{0} global i32 {1};", identifier, value);
        }
    }
}