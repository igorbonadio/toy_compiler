grammar Likely;

r : (expr NEWLINE+)* expr? ;

expr : literal
     | attr
     | list
     | if_expr
     | while_expr
     | for_expr
     | expr bin_op expr
     | '(' expr ')'
     ;

list : '(' ')'
     | '(' expr (',' expr)* ')'
     | '(' NEWLINE* (expr NEWLINE+)* expr? ')'
     ;

for_expr : 'for' '(' ID 'in' expr ')' expr
         | 'for' '(' ID 'in' expr ')' '{' expr* '}'
         ;

while_expr : 'while' '(' expr ')' expr
           | 'while' '(' expr ')' '{' expr* '}'
           ;

if_expr : 'if' '(' expr ')' expr 'else' expr
        | 'if' '(' expr ')' '{' expr* '}' 'else' '{' expr* '}'
        ;

attr : ID '=' expr
     ;

bin_op : '+'
       | '-'
       | '*'
       | '/'
       | '=='
       | '>='
       | '<='
       | '>'
       | '<'
       | ':'
       ;

literal : ID
        | number
        | STRING
        ;

number : INTEGER
       | FLOAT
       ;

ID      : [a-zA-z_][a-zA-z0-9_]* ;
INTEGER : ('-')? NUM ;
FLOAT   : ('-')? NUM '.' NUM ;
STRING  : '"' (~["\\\r\n])* '"' ;

NEWLINE : ('\r'? '\n' | '\r') SPACES? ;

SKIP : (COMMENT | SPACES) -> skip ;

fragment NUM : [0-9]+ ;
fragment COMMENT : '#' ~[\r\n]* ;
fragment SPACES  : [ \t]+ ;
