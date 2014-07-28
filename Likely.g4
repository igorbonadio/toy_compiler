grammar Likely;

r : expr* ;

expr : literal
     | attr
     | if_expr
     | expr bin_op expr
     | '(' expr ')'
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