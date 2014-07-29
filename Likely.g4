grammar Likely;

tokens { INDENT, DEDENT }

@lexer::members {

  private java.util.Queue<Token> tokens = new java.util.LinkedList<>();
  private java.util.Stack<Integer> indents = new java.util.Stack<>();
  private int opened = 0;

  @Override
  public void emit(Token t) {
    super.setToken(t);
    tokens.offer(t);
  }

  @Override
  public Token nextToken() {
    if (_input.LA(1) == EOF && !this.indents.isEmpty()) {
      this.emit(new CommonToken(LikelyParser.NEWLINE, "\n"));
      while (!indents.isEmpty()) {
        this.emit(new CommonToken(LikelyParser.DEDENT, "DEDENT"));
        indents.pop();
      }
    }
    Token next = super.nextToken();
    return tokens.isEmpty() ? next : tokens.poll();
  }

  static int getIndentationCount(String spaces) {
    int count = 0;
    for (char ch : spaces.toCharArray()) {
      switch (ch) {
        case '\t':
          count += 8 - (count % 8);
          break;
        default:
          count++;
      }
    }
    return count;
  }
}

file_input : ( NEWLINE | stmt )*
           ;

stmt : expr NEWLINE
     | expr EOF
     ;

expr : literal
     | attr
     | list
     | if_expr
     | while_expr
     | for_expr
     | expr bin_op expr
     | func_def
     | func_call
     | return_expr
     | '(' expr ')'
     ;

func_call : func '(' func_args? ')'
          ;

func : ID
     | '(' returned_func ')'
     ;

returned_func : func
              | if_expr
              | while_expr
              | for_expr
              | func_def
              | func_call
              ;

func_args : expr (',' expr)*
          ;

func_def : 'function' '(' func_params? ')' block
         ;

func_params : ID (',' ID)*
            ;

return_expr : 'return' expr
            ;

list : '(' ')'
     | '(' expr (',' expr)* ')'
     | '(' NEWLINE* (expr NEWLINE+)* expr? ')'
     ;

for_expr : 'for' '(' ID 'in' expr ')' block
         ;

while_expr : 'while' '(' expr ')' block
           ;

if_expr : 'if' '(' expr ')' block 'else' block
        ;

block : ':' expr
      | ':' NEWLINE INDENT stmt+ DEDENT NEWLINE?
      | '{' (NEWLINE* stmt)+ '}'
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

FOR      : 'for' ;
IN       : 'in' ;
WHILE    : 'while' ;
IF       : 'if' ;
FUNCTION : 'function' ;
RETURN   : 'return' ;

ID      : [a-zA-z_][a-zA-z0-9_]* ;
INTEGER : ('-')? NUM ;
FLOAT   : ('-')? NUM '.' NUM ;
STRING  : '"' (~["\\\r\n])* '"' ;

OPEN_PAREN : '(' {opened++;};
CLOSE_PAREN : ')' {opened--;};
OPEN_BRACK : '[' {opened++;};
CLOSE_BRACK : ']' {opened--;};
OPEN_BRACE : '{' {opened++;};
CLOSE_BRACE : '}' {opened--;};

NEWLINE : ('\r'? '\n' | '\r') SPACES?
  {
    String spaces = getText().replaceAll("[\r\n]+", "");
    int next = _input.LA(1);

    if (opened > 0) {

    } else if (next == '\r' || next == '\n' || next == '#') {
      skip();
    } else {
      emit(new CommonToken(NEWLINE, "\n"));

      int indent = getIndentationCount(spaces);
      int previous = indents.isEmpty() ? 0 : indents.peek();

      if (indent == previous) {
        // skip indents of the same size as the present indent-size
        skip();
      } else if (indent > previous) {
        indents.push(indent);
        emit(new CommonToken(LikelyParser.INDENT, "INDENT"));
      }
      else {
        while(!indents.isEmpty() && indents.peek() > indent) {
          emit(new CommonToken(LikelyParser.DEDENT, "DEDENT"));
          indents.pop();
        }
        emit(new CommonToken(NEWLINE, "\n"));
      }
    }
  }
  ;

SKIP : (COMMENT | SPACES) -> skip ;

fragment NUM : [0-9]+ ;
fragment COMMENT : '#' ~[\r\n]* ;
fragment SPACES  : [ \t]+ ;
