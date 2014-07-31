grammar Likely;

file_input : ( NEWLINE | stmt )*
           ;

stmt : expr (NEWLINE | ';')
     ;

expr : literal
     | attr
     | seq
     | '(' expr ')'
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

seq : '[' seq_body? ']'
    ;

seq_body : expr (',' expr)*
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
