grammar Likely;

file_input : ( NEWLINE | stmt )*
           ;

stmt : expr (NEWLINE | ';')
     ;

expr : literal
     | attr
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

attr : ID '=' expr
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

ID      : [a-zA-z_][a-zA-z0-9_]* ;
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
