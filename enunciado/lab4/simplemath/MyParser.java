import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;

class MyParser {

    public static void main(String args[]) throws Exception {
        if(args.length >= 1)
            System.setIn(new FileInputStream(args[0]));

        // create a CharStream that reads from standard input
        final CharStream input = CharStreams.fromStream(System.in);

        // create a lexer that feeds off of input CharStream
        final SimpleMathLexer lexer = new SimpleMathLexer(input);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        final SimpleMathParser parser = new SimpleMathParser(tokens);

        // begin parsing at prog rule
        final ParseTree tree = parser.root();

        // creates visitor instance to visit parse tree and get compilation log
        final MyVisitor myVisitor = new MyVisitor();

        // visits parse tree
        myVisitor.visit(tree);

        // gets compilation log
        final String compilationLog = myVisitor.getCompilationLog();
        final String generatedCode = myVisitor.getGeneratedCode();

        // prints compilation log as a final result from the program
//        System.out.print(compilationLog);
        System.out.print(generatedCode);
    }

}