grammar SimpleMath;

root : s;

s : s SEMICOLON s           #SSemicolonS
  | var_declaration         #SVarDeclaration
  | func_declaration        #SFuncDeclaration
  | EOF                     #SEOF
  ;

var_declaration : VAR ID EQ expression;

func_declaration : FUNC ID LPAREN func_param_list RPAREN func_body;

func_param_list : ID (COMMA ID)*;

func_body : expression;

func_invocation : ID LPAREN func_invocation_param_list RPAREN;

func_invocation_param_list : func_invocation_param (COMMA func_invocation_param)*;

func_invocation_param : ID
                      | NUM
                      ;

expression  : expression SUB expression_t
            | expression ADD expression_t
            | expression_t
            ;

expression_t : expression_t MUL expression_f
             | expression_t DIV expression_f
             | expression_f
             ;

expression_f : declared_id
             | NUM
             | LPAREN expression RPAREN
             | func_invocation
             ;

declared_id : ID;


WHITESPACE
 : [ \t\r\n] -> skip
 ;


VAR         : 'var';
FUNC        : 'func';

EQ          : '=';
SEMICOLON   : ';';

LPAREN  :   '(';
RPAREN  :   ')';
ADD     :   '+';
MUL     :   '*';
SUB     :   '-';
COMMA   :   ',';
DIV     :   '/';

ID          : [a-zA-Z_][a-zA-Z0-9_]*;
NUM         : [0-9]+;

ERROR_CHAR : . ;
