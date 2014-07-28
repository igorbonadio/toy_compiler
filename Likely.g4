grammar Likely;

r : expr* ;
expr : ID
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