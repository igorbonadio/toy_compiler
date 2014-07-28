grammar Likely;

r : expr* ;

expr : literal
     | attr
     | if_expr
     | while_expr
     | for_expr
     | expr bin_op expr
     | '(' expr ')'
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
INTEGER : ('-')?[0-9][0-9]* ;
FLOAT   : ('-')?[0-9][0-9]*'.'[0-9][0-9]* ;
STRING  : '"' (~["\\\r\n])* '"' ;

WS      : [ \t\r\n]+ -> skip ;