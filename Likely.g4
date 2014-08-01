grammar Likely;

file_input : ( NEWLINE | stmt )*
           ;

stmt : expr (NEWLINE | ';' | EOF)
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
     | constructor_call
     | comp_expr
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

seq : '[' list_body? ']'
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

list_body : list_body_fat
          | list_body_thin
          ;

list_body_fat : expr (',' expr)*
              ;

list_body_thin : NEWLINE+ (expr NEWLINE+)*
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

constructor_call : func_call ':' fat_expr
                 ;

fat_expr : expr
         | '{' (expr ';')* expr? '}'
         ;

comp_expr : if_expr
          | for_expr
          | while_expr
          | func_def
          ;

if_expr : 'if' '(' expr ')' ':' fat_expr 'else' ':' fat_expr
        ;

for_expr : 'for' '(' ID '<-' expr ':' expr ')' ':' fat_expr
         ;

while_expr : 'while' '(' expr ')' ':' fat_expr
           ;

func_def : 'function' '(' func_params? ')' ':' fat_expr
         ;

func_params : ID (',' ID)*
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
