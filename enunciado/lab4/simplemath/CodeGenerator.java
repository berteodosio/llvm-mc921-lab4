import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

class CodeGenerator {

    private String generatedCode = "";

    private final LlvmGenerator llvmGenerator = new LlvmGenerator();
    private final TempGenerator tempGenerator = new TempGenerator();

    private final List<String> globalVariableSet = new ArrayList<>();
    private final Set<String> currentFunctionArguments = new HashSet<>();

    private static final String INITIALIZATION_FUNCTION_NAME = "batata";

    String getFullGeneratedCode() {
        return generatedCode;
    }

    private void generateTemp(final ParserRuleContext ctx) {
        if (ctx.getText().chars().filter(ch -> ch == '(').count() > 1 &&
                ctx.getText().chars().filter(ch -> ch == ')').count() > 1) {
            generateExpression((ParserRuleContext)ctx.children.get(0));

        } else if (ctx.getText().chars().filter(ch -> ch == '(').count() == 1 &&
                ctx.getText().chars().filter(ch -> ch == ')').count() == 1) {
            String ctxText = ctx.getText();
            String funName = ctxText.split("[(]")[0];
            String[] arguments = ctxText.split("[(]")[1]
                    .replaceAll("[)]", "")
                    .split(",");

            List<String> argumentsList = Arrays.stream(arguments).map(it -> {
                if (globalVariableSet.contains(it)) {
                    String tempVar = tempGenerator.generateTemp();
                    generatedCode += llvmGenerator.generateTempVarFromGlobal(tempVar, it);
                    tempGenerator.fetchNameFromStack();
                    return tempVar;
                } else {
                    return it;
                }
            }).collect(Collectors.toList());

            String tempName = tempGenerator.generateTemp();
            generatedCode += llvmGenerator.generateTempVarFromFunction(tempName,
                    llvmGenerator.generateFunctionCall(funName, argumentsList));
        } else {
            final String tempName = tempGenerator.generateTemp();
            final String ctxText = ctx.getText();
            final String tempVarValue;

            if (globalVariableSet.contains(ctxText) && !currentFunctionArguments.contains(ctxText)) {
                generatedCode += llvmGenerator.generateTempVarFromGlobal(tempName, ctxText);
            } else if (!ctxText.matches(".*\\d.*")) {
                tempVarValue = "%" + ctxText;
                generatedCode += llvmGenerator.generateTempVar(tempName, tempVarValue);
            }  else {
                tempVarValue = ctxText;
                generatedCode += llvmGenerator.generateTempVar(tempName, tempVarValue);

            }
        }
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
            String ctxText = ctx.getText();
            if (ctxText.contains("(")) {
                generateTemp(ctx);
            } else {
                String tempName = tempGenerator.generateTemp();
                generatedCode += llvmGenerator.generateTempVar(tempName, ctx.getText());
            }
        } else if (((ParserRuleContext)ctx.children.get(0)).children.size() == 1) {
            generateTemp(ctx);
        } else {
            generateExpression((ParserRuleContext) ctx.children.get(0));
        }
    }

    void generateGlobalVar(final SimpleMathParser.SVarDeclarationContext ctx) {
        final String globalVarName = ctx.var_declaration().ID().getText();
        globalVariableSet.add(globalVarName);

        generatedCode += llvmGenerator.generateFunctionFirstLineStart(getSuffixedGlobalVarName(globalVarName));
        generatedCode += llvmGenerator.generateFunctionFirstLineEnd();
        this.generateFunctionBody(ctx.var_declaration().expression());
    }

    private String getSuffixedGlobalVarName(final String globalVarName) {
        return globalVarName + "__init_function";
    }

    void generateFunctionHeader(final SimpleMathParser.SFuncDeclarationContext ctx) {
        generatedCode += llvmGenerator.generateFunctionFirstLineStart(ctx.func_declaration().ID().getText());
    }

    void generateFunctionParameterList(final SimpleMathParser.Func_param_listContext ctx) {
        currentFunctionArguments.clear();
        for (int i = 0; i < ctx.ID().size(); i++) {
            final TerminalNode parameterName = ctx.ID(i);
            currentFunctionArguments.add(parameterName.getText());
            generatedCode += llvmGenerator.generateFunctionParameterName(parameterName.getText(), i);
        }

        generatedCode += llvmGenerator.generateFunctionFirstLineEnd();
    }

    void generateFunctionBody(final SimpleMathParser.Func_bodyContext ctx) {
        this.generateFunctionBody(ctx.expression());
    }

    void generateFunctionBody(final SimpleMathParser.ExpressionContext ctx) {
        this.generateExpression(ctx);
        generatedCode += llvmGenerator.generateFunctionBody(tempGenerator.fetchNameFromStack());
        generatedCode += llvmGenerator.generateFunctionLastLine();
        currentFunctionArguments.clear();
    }

    void generateInitializationFunction() {
        generatedCode += "\n\n";

        generatedCode += "; init global var declarations\n";
        for (final String globalVariableName : globalVariableSet) {
            generatedCode += llvmGenerator.generateGlobalVar(globalVariableName, "0");
        }
        generatedCode += "; end global var declarations\n\n";

        generatedCode += llvmGenerator.generateFunctionFirstLineStart(INITIALIZATION_FUNCTION_NAME);
        generatedCode += llvmGenerator.generateFunctionFirstLineEnd();

        for (final String globalVariableName : globalVariableSet) {
            final String tempVariable = tempGenerator.generateTemp();
            generatedCode += llvmGenerator.generateCallInstruction(tempVariable, getSuffixedGlobalVarName(globalVariableName));
            generatedCode += llvmGenerator.generateStoreInstruction(tempVariable, globalVariableName);
        }

        generatedCode += llvmGenerator.generateFunctionBody("0");
        generatedCode += llvmGenerator.generateFunctionLastLine();
    }

    class LlvmGenerator {

        String generateCallInstruction(final String returnVariableName, final String functionName) {
            return MessageFormat.format("{0} = call i32 @{1}()\n", returnVariableName, functionName);
        }

        String generateGlobalVar(final String identifier, final String value) {
            return MessageFormat.format("@{0} = global i32 {1};\n", identifier, value);
        }

        String generateFunctionCall(final String functionName, final List<String> parameters) {
            String call = MessageFormat.format("call i32 @{0}(", functionName);
            call += parameters.stream().map(it ->"i32 " + it).collect(Collectors.joining(","));

            return call + ")\n";
        }

        String generateFunctionFirstLineStart(final String functionName) {
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
            return "\n}\n";
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
            return MessageFormat.format("{0} = sdiv i32 {1},{2}\n", tempName, param1, param2);
        }

        String generateTempVar(final String tempName, final String value) {
            return MessageFormat.format("{0} = add i32 {1}, 0\n", tempName, value);
        }

        String generateTempVarFromFunction(final String tempName, final String value) {
            return MessageFormat.format("{0} = {1}\n", tempName, value);
        }

        String generateTempVarFromGlobal(final String tempName, final String globalName) {
            return MessageFormat.format("{0} = load i32, i32* @{1}\n", tempName, globalName);
        }

        String generateStoreInstruction(final String temporaryName, final String destinationName) {
            return MessageFormat.format("store i32 {0}, i32* @{1}\n", temporaryName, destinationName);
        }
    }

    class TempGenerator {
        private Stack<String> tempStack;

        TempGenerator() {
            tempStack = new Stack<>();
        }

        public String generateTemp() {
            String tempName = UUID.randomUUID().toString().replaceAll("-", "");
            tempName = "%" +  tempName.replaceAll("[\\d.]", "");
            tempStack.push(tempName);

            return tempName;
        }

        public String fetchNameFromStack() {
            return tempStack.pop();
        }
    }
}