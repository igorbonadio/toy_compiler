grammar Likely;

tokens { INDENT, DEDENT }

@lexer::members {
  private java.util.Queue<Token> tokens = new java.util.LinkedList<>();
  private java.util.Stack<Integer> indents = new java.util.Stack<>();

  @Override
  public void emit(Token t) {
    super.setToken(t);
    tokens.offer(t);
  }

  @Override
  public Token nextToken() {

    // Check if the end-of-file is ahead and there are still some DEDENTS expected.
    if (_input.LA(1) == EOF && !this.indents.isEmpty()) {

      // First emit an extra line break that serves as the end of the statement.
      this.emit(new CommonToken(LikelyParser.NEWLINE, "\n"));

      // Now emit as much DEDENT tokens as needed.
      while (!indents.isEmpty()) {
        this.emit(new CommonToken(LikelyParser.DEDENT, "DEDENT"));
        indents.pop();
      }
    }

    Token next = super.nextToken();

    return tokens.isEmpty() ? next : tokens.poll();
  }

  // Calculates the indentation of the provided spaces, taking the
  // following rules into account:
  //
  // "Tabs are replaced (from left to right) by one to eight spaces
  //  such that the total number of characters up to and including
  //  the replacement is a multiple of eight [...]"
  //
  //  -- https://docs.python.org/3.1/reference/lexical_analysis.html#indentation
  static int getIndentationCount(String spaces) {

    int count = 0;

    for (char ch : spaces.toCharArray()) {
      switch (ch) {
        case '\t':
          count += 8 - (count % 8);
          break;
        default:
          // A normal space char.
          count++;
      }
    }

    return count;
  }
}

file_input : NEWLINE* header ( NEWLINE | statement )*
           ;

header : importPackage*
       ;

importPackage : 'import' (ID | '.')? STRING (NEWLINE | ';' | EOF)
              ;

statement : expression (NEWLINE? | ';' | EOF)
          ;

expression : ID
     | literal
     | attribution
     | sequence
     | expression ARROW expression
     | expression op expression
     | NOT expression
     | OPEN_PAREN expression CLOSE_PAREN
     | functionCall
     | distribution
     | returnExpression
     | objectMessage
     | constructorCall
     | complexExpression
     ;

literal : number
        | STRING
        | bool
        ;

number : INTEGER
       | FLOAT
       ;

bool : 'true'
     | 'false'
     ;

attribution : (ID | objectMessage) '=' expression
            ;

sequence : '[' listBody? ']'
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
   | 'and'
   | 'or'
   ;

functionCall : func list+
             ;

func : ID
     | objectMessage
     ;

list : '(' listBody? ')'
     ;

listBody : fatListBody
          | thinListBody
          ;

fatListBody : expression (',' expression)*
            ;

thinListBody : NEWLINE+ INDENT (expression NEWLINE+)* DEDENT
             ;

distribution : 'Prob' '(' distBody? ')'
     ;

distBody : fatDistBody
         | thinDistBody
         ;

fatDistBody : probability (',' probability)*
            ;

thinDistBody : NEWLINE+ INDENT (probability NEWLINE+)* DEDENT
             ;

probability : probabilityVariable '=' expression
     ;

probabilityVariable : joint_vars
                    | cond_vars
                    ;

joint_vars : sample
           | '(' var_list? ')'
           ;

sample : ID+
       ;

var_list : sample (',' sample)*
         ;

cond_vars : joint_vars '|' joint_vars
          ;

returnExpression : 'return' expression
            ;

objectMessage : obj ('.' ID list?)+
              ;

obj : literal
    | ID
    | sequence
    | probability
    | '(' expression ')'
    ;

constructorCall : functionCall ':' block
                 ;

block : fat_expr
      | thin_expr
      ;

thin_expr : NEWLINE+ INDENT (expression NEWLINE*)* DEDENT
          ;

fat_expr : expression
         | '{' (expression ';')* expression? '}'
         ;

complexExpression : if_expr
          | for_expr
          | while_expr
          | func_def
          ;

if_expr : 'if' '(' expression ')' ':' block 'else' ':' block
        ;

for_expr : 'for' '(' ID '<-' expression ':' expression ')' ':' block
         ;

while_expr : 'while' '(' expression ')' ':' block
           ;

func_def : 'function' '(' func_params? ')' ':' block
         ;

func_params : ID (',' ID)*
            ;


IMPORT   : 'import' ;
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
PROB     : 'Prob' ;

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

DOT   : '.' ;
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

NEWLINE : ('\r'? '\n' | '\r') SPACES?
        {
          String spaces = getText().replaceAll("[\r\n]+", "");
          int next = _input.LA(1);

          if (next == '\r' || next == '\n' || next == '#') {
            // If we're on a blank line, ignore all indents,
            // dedents and line breaks.
            skip();
          }
          else {
            emit(new CommonToken(NEWLINE, "\n"));

            int indent = getIndentationCount(spaces);
            int previous = indents.isEmpty() ? 0 : indents.peek();

            if (indent == previous) {
              // skip indents of the same size as the present indent-size
              // skip();
            }
            else if (indent > previous) {
              indents.push(indent);
              emit(new CommonToken(LikelyParser.INDENT, "INDENT"));
            }
            else {
              // Possibly emit more than 1 DEDENT token.
              while(!indents.isEmpty() && indents.peek() > indent) {
                emit(new CommonToken(LikelyParser.DEDENT, "DEDENT"));
                indents.pop();
              }
            }
          }
        }
        ;

SKIP : (COMMENT | SPACES) -> skip ;

fragment NUM : [0-9]+ ;
fragment COMMENT : '#' ~[\r\n]* ;
fragment SPACES  : [ \t]+ ;
fragment ID_START : '_' | [A-Z] | [a-z] ;
fragment ID_CONTINUE : ID_START | [0-9] ;
