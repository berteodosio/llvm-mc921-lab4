#!/bin/bash
#set -x

#compiling the Sum.g4 in java files
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor SimpleMath.g4
#exporting the class path
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
#compiling the .java generated from Sum.g4 with MyParser.java and AddVisitor.java
javac *.java

EXPRESSION_1="var a = 0;"

EXPRESSION_2="func a(x) 2 + x * 5;"

EXPRESSION_3="var a = 25;
func f1(x) 10 + x * a;
func f2(a, x) f1(a) * (x + 30);
var b = f2(a, a);
func f3(x, y, z) x + y - z + 10;"

EXPRESSION_4="var g1 = 1;
var g2 = 10;
var g3 = g1 + 5;
var g4 = 15 + 3 * 2;
func _f1(a, b) g2 * 10 + (g3 - 2) * 3;
var _g1 = _f1(g4, 3);
func f1 (g1, g2, a, b) (g1 + g2 + g3) * _g1 + a / b;
func function1 (bar, par, car) f1(g1, g2, 9, car) * _f1(bar, par);"

EXPRESSION_5="var a = 0;
var b = 15;
var c = d;
var d = a(10);
var e = d + 20;
var f = a * b + 10 / c;
func A (g) g + a * (2 + b);
func B (g, a) g * a + (f - e + 5) / 2;
func C (A) A - 3;
func D (B) B * A(e);
func E (g) g + A(g) * B(f, g);
func F (A) B + 32 * C(b);"

EXPRESSION_6="func a (a) (32 * a + 5) / 3;
var a = 6;
var b = a(5);
func c(b) a + 10 * b;"

EXPRESSION_7="func A (A) A * 32;
var b = A(3);
var a = A + 3;
var b = A(a) * 30;
func B (a, b) (a + b - c) / 12;
var c = c(23);
func c (A, V) B(A, V) * 7 - 15;
var d = (C(23, a) / B(32, a));"

EXPRESSION_8="func _le_2func(a, b, c, d) ((a + b - c) * d);
var _23 = _le_2func(1,2, 3);"

EXPRESSION_IN_USE=${EXPRESSION_8}

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