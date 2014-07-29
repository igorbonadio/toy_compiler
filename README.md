Likely Language
===============

Likely is a language to specify probabilistic models.

GRAMMAR
=======

  program := ( NEWLINE | stmt )*

  stmt := expr NEWLINE

  expr := literal | attr | list | pair | comp\_expr | binop | func\_call | prob | '(' expr ')'

  literal := ID | number | STRING

  number := INTEGER | FLOAT

  attr := ID '=' expr

  list := '(' list_body? ')'

  list_body := expr (',' expr)*

  pair := expr '->' expr

  binop := expr op expr

  op := '+' | '-' | '*' | '/' | '==' | '>=' | '<=' | '>' | '<' | ':'

  func\_call := func list

  func := ID | '(' expr ')'

  prob := prob\_vars '=' number

  prob\_var := joint\_vars | cond\_vars

  joint\_vars := ID (';' ID)*

  cond_vars := joint\_vars '|' joint\_vars

  comp\_expr := if\_expr | for\_expr | while\_expr | func\_def

  if\_expr := 'if' '(' expr ')' ':' expr 'else' ':' expr

  for\_expr := 'for' '(' ID '<-' expr ':' expr ')' ':' expr

  while\_expr := 'while' '(' expr ')' ':' expr

  func\_def := 'function' '(' func\_params? ')' ':' expr

  func\_params := ID (',' ID)*
