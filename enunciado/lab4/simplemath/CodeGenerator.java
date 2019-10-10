import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.text.MessageFormat;
import java.util.Stack;
import java.util.UUID;

class CodeGenerator {

    private String generatedCode = "";

    private final LlvmGenerator llvmGenerator = new LlvmGenerator();
    private final TempGenerator tempGenerator = new TempGenerator();

    String getFullGeneratedCode() {
        return generatedCode;
    }

    void generateExpression(final ParserRuleContext ctx) {
        if (ctx.children.size() == 3) {
            if (ctx.children.get(0).getText().equals("(")) {
                generateExpression((ParserRuleContext) ctx.children.get(1));
            } else {
                generateExpression((ParserRuleContext) ctx.children.get(0));
                generateExpression((ParserRuleContext) ctx.children.get(2));
                String leftTemp = tempGenerator.fetchNameFromStack();
                String rightTemp = tempGenerator.fetchNameFromStack();
                TerminalNode terminalNode = (TerminalNode) ctx.children.get(1);
                switch (terminalNode.getText()) {
                    case "+":
                        generatedCode += llvmGenerator.generateAddOp(tempGenerator.generateTemp(), leftTemp, rightTemp);
                        break;
                    case "-":
                        generatedCode += llvmGenerator.generateSubOp(tempGenerator.generateTemp(), rightTemp, leftTemp);
                        break;
                    case "/":
                        generatedCode += llvmGenerator.generateDivOp(tempGenerator.generateTemp(), rightTemp, leftTemp);
                        break;
                    case "*":
                        generatedCode += llvmGenerator.generateMulOp(tempGenerator.generateTemp(), leftTemp, rightTemp);
                        break;
                }
            }
        } else if (ctx.children.get(0) instanceof TerminalNodeImpl) {
            String tempName = tempGenerator.generateTemp();
            generatedCode += llvmGenerator.generateTempVar(tempName, ctx.getText());
        } else if (((ParserRuleContext)ctx.children.get(0)).children.size() == 1) {
            String tempName = tempGenerator.generateTemp();
            generatedCode += llvmGenerator.generateTempVar(tempName, ctx.getText());
        } else {
            generateExpression((ParserRuleContext) ctx.children.get(0));
        }
    }

    void generateGlobalVar(final SimpleMathParser.SVarDeclarationContext ctx) {
        // TODO: parse expression
        this.generateExpression(ctx.var_declaration().expression());
        generatedCode += llvmGenerator.generateGlobalVar(ctx.var_declaration().ID().getText(), tempGenerator.fetchNameFromStack());
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
        this.generateExpression(ctx.expression());
        generatedCode += llvmGenerator.generateFunctionBody(tempGenerator.fetchNameFromStack());
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

        String generateAddOp(final String tempName, final String param1, final String param2) {
            return MessageFormat.format("{0} = add i32 {1},{2}\n", tempName, param1, param2);
        }

        String generateSubOp(final String tempName, final String param1, final String param2) {
            return MessageFormat.format("{0} = sub i32 {1},{2}\n", tempName, param1, param2);
        }

        String generateMulOp(final String tempName, final String param1, final String param2) {
            return MessageFormat.format("{0} = mul i32 {1},{2}\n", tempName, param1, param2);
        }

        String generateDivOp(final String tempName, final String param1, final String param2) {
            return MessageFormat.format("{0} = div i32 {1},{2}\n", tempName, param1, param2);
        }

        String generateTempVar(final String tempName, final String value) {
            return MessageFormat.format("{0} = i32 {1}\n", tempName, value);
        }
    }

    class TempGenerator {
        private Stack<String> tempStack;

        TempGenerator() {
            tempStack = new Stack<>();
        }

        public String generateTemp() {
            String tempName = "%" + UUID.randomUUID().toString().replaceAll("-", "");
            tempStack.push(tempName);

            return tempName;
        }

        public String fetchNameFromStack() {
            return tempStack.pop();
        }
    }
}