#!/bin/bash
#set -x

#compiling the Sum.g4 in java files
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor SimpleMath.g4
#exporting the class path
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
#compiling the .java generated from Sum.g4 with MyParser.java and AddVisitor.java
javac *.java

EXPRESSION_0="var sm_main = 5;"

EXPRESSION_1="func a(x) x;
var sm_main = a(10);"

EXPRESSION_2="var a = 10 - 5 - 4;
func f(x) x + 2 * a;
var sm_main = f(5);"

EXPRESSION_3="var a = 5;
var b = 30 / a + 2;
func f(a) a + b - 3 * 3;
var sm_main = 5 + a - f(b);"

EXPRESSION_4="func f(a, b, c, d) a * a / b + c * c / d - d;
var sm_main = f(5, 6, 7, 8);"

EXPRESSION_5="var a = 10;
var b = 5;
func function(c) (a + b * 2) / c;
var sm_main = function(10) - 1;"

EXPRESSION_6="var a = 1;
var b = a + 2 * a + 3 - 2;
func f1(a) b * 8 / (3 + a);
func f2(b) b * a + b * (b - 2);
var c = ((f1(2) + f2(a) - 15)) / 2;
func f3(a, b) a * (c - b);
var d = 0 - 10;
var sm_main = f3(c, d);"

EXPRESSION_7="var a = 1;
var b = 1;
var c = b + a;
var d = c + b;
var e = d + c;
var f = a + b + c + d + e;
func g(a, b, c, d) a + b + c + d;
var sm_main = g(2, 3, 4, 5) - f * 2;"

EXPRESSION_8="func a(x) (x + 10) / 2;
var b = a(2);
func c(x) (a(7) - b) * 2;
var d = (c(3) - a(2)) * (c(2) + a(3));
func e(a, b) ((b + a) * c(d) + 15) / (0 - 5);
var sm_main = e(10, d) + c(b);"

EXPRESSION_9="var sm_main = (30 + 12) * ((15 - 2) / 3 + 7 - 2) - 77 * (0 - 1) - 42;"

EXPRESSION_IN_USE=${EXPRESSION_0}

#feeding a string and reading the tokens
#echo "${EXPRESSION_IN_USE}" | java org.antlr.v4.gui.TestRig SimpleMath root -tokens

# #feeding a string and reading tree in list style
#echo "var a = 0;" | java org.antlr.v4.gui.TestRig SimpleMath root -tree

# #feeding a string and printing a graphical tree
#echo "${EXPRESSION_IN_USE}" | java org.antlr.v4.gui.TestRig SimpleMath root -gui

#execute the implemented visitor
echo "${EXPRESSION_IN_USE}" | java MyParser

clear() {
    rm *.class
}

clear

#clang -S -emit-llvm code.c
#@a = global i32 10