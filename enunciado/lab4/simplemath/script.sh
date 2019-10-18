#!/bin/bash

# compiling the SimpleMath.g4 in java files
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor SimpleMath.g4

# exporting the class path
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"

# compiling the .java generated from SimpleMath.g4 with MyParser.java and MyVisitor.java
javac *.java

# execute the implemented visitor to generate llvm code
# if you want to test a custom llvm code, comment this line to avoid regenerating code.ll
java MyParser < $1 > code.ll

# Compile LLVM CODE
llc code.ll

# Compile C source file with assembly code
gcc printer.c code.s -o printer

# execute printer
./printer
