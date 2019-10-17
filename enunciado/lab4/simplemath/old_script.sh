#!/bin/bash

# compiling the SimpleMath.g4 in java files
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor SimpleMath.g4

# exporting the class path
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"

# compiling the .java generated from SimpleMath.g4 with MyParser.java and MyVisitor.java
javac *.java

# execute the implemented visitor for all available tests
java MyParser < test1.sm > result1.txt
java MyParser < test2.sm > result2.txt
java MyParser < test3.sm > result3.txt
java MyParser < test4.sm > result4.txt
java MyParser < test5.sm > result5.txt
java MyParser < test6.sm > result6.txt
java MyParser < test7.sm > result7.txt
java MyParser < test8.sm > result8.txt

# finishing displaying test7.sm parse tree
java org.antlr.v4.gui.TestRig SimpleMath root -gui < tests/test8.sm