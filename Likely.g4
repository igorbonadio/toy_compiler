grammar Likely;

file_input : ( NEWLINE | stmt )*
           ;

stmt : expr (NEWLINE | ';')
     ;

expr : literal
     | attr
     | seq
     | expr '->' expr
     | expr op expr
     | '(' expr ')'
     | func_call
     | prob
     | return_expr
     | obj_msg
     ;

literal : ID
        | number
        | STRING
        | bool
        ;

number : INTEGER
       | FLOAT
       ;

bool : 'true'
     | 'false'
     ;

attr : (ID | obj_msg) '=' expr
     ;

seq : '[' seq_body? ']'
    ;

seq_body : expr (',' expr)*
         ;

op : '+'
   | '-'
   | '*'
   | '/'
   | '=='
   | '!='
   | '>='
   | '<='
   | '>'
   | '<'
   | ':'
   ;

func_call : func list+
          ;

func : ID
     | obj_msg
     ;

list : '(' list_body? ')'
     ;

list_body : expr (',' expr)*
          ;

prob : prob_vars '=' number
     ;

prob_vars : joint_vars
          | cond_vars
          ;

joint_vars : ID
           | list
           ;

cond_vars : joint_vars '|' joint_vars
          ;

return_expr : 'return' expr
            ;

obj_msg : obj ('.' ID list?)+
        ;

obj : literal
    | seq
    | prob
    | '(' expr ')'
    ;



FOR      : 'for' ;
IN       : '<-' ;
WHILE    : 'while' ;
IF       : 'if' ;
FUNCTION : 'function' ;
RETURN   : 'return' ;
TRUE     : 'true' ;
FALSE    : 'false' ;
ARROW    : '->' ;
NOT      : 'not' ;
AND      : 'and' ;
OR       : 'or' ;

ID      : ID_START ID_CONTINUE* ;
INTEGER : ('-')? NUM ;
FLOAT   : ('-')? NUM '.' NUM ;
STRING  : '"' (~["\\\r\n])* '"' ;

OPEN_PAREN  : '(' ;
CLOSE_PAREN : ')' ;
OPEN_BRACK  : '[' ;
CLOSE_BRACK : ']' ;
OPEN_BRACE  : '{' ;
CLOSE_BRACE : '}' ;

ADD   :'+' ;
SUB   : '-' ;
MUL   : '*' ;
DIV   : '/' ;
EQL   : '==' ;
DIF   : '!=' ;
GEQ   : '>=' ;
LEQ   : '<=' ;
GRE   : '>' ;
LES   : '<' ;

COLON     : ':' ;
COMMA     : ',' ;
SEMICOLON : ';' ;

NEWLINE : ('\r'? '\n' | '\r') ;

SKIP : (COMMENT | SPACES) -> skip ;

fragment NUM : [0-9]+ ;
fragment COMMENT : '#' ~[\r\n]* ;
fragment SPACES  : [ \t]+ ;
fragment ID_START : '_' | [A-Z] | [a-z] ;
fragment ID_CONTINUE : ID_START | [0-9] ;
