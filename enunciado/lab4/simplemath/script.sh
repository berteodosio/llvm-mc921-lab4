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

# TODO: REMOVE THIS LINE
#export PATH=$PATH:/usr/local/opt/llvm/bin/llc

# Compile LLVM CODE
# FIXME: change to llc
/usr/local/opt/llvm/bin/llc code.ll

# Compile C source file with assembly code
gcc printer.c code.s -o printer

