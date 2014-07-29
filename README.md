# Likely Language

Likely is a language to specify probabilistic models. But not just that! In fact Likely is a full language that will help you to build sequence analysis tools.

It is part of a bigger project, [Likely Framework](https://github.com/igorbonadio/likely), that provides the infrastructure to build and run probabilistic models.

## Examples

### Arithmetic Expressions

Likely's arithmetic expressions are very similar to languages like C, C++, Java, Python and Ruby. The only difference is that there isn't operator precedence. So, use parentheses!

```python
  1 + 2       # => 3
  2 - 1       # => 1
  4 / 2       # => 2
  2 * 3       # => 6
  1 * 2 + 3   # => 5
  1 + 2 * 3   # => 9
  1 + (2 * 3) # => 7
```

## Grammar

```
  program := ( NEWLINE | stmt )*

  stmt := expr NEWLINE

  expr := literal | attr | list | pair | comp_expr | binop | func_call | constructor_call | prob | '(' expr ')'

  literal := ID | number | STRING

  number := INTEGER | FLOAT

  attr := ID '=' expr

  list := '(' list_body? ')'

  list_body := expr (',' expr)*

  pair := expr '->' expr

  binop := expr op expr

  op := '+' | '-' | '*' | '/' | '==' | '>=' | '<=' | '>' | '<' | ':'

  func_call := func list

  func := ID | '(' expr ')'

  constructor_call := func_call ':' fat_expr

  fat_expr := expr (';' expr)

  prob := prob_vars '=' number

  prob_var := joint_vars | cond_vars

  joint_vars := ID (';' ID)*

  cond_vars := joint_vars '|' joint_vars

  comp_expr := if_expr | for_expr | while_expr | func_def

  if_expr := 'if' '(' expr ')' ':' fat_expr 'else' ':' fat_expr

  for_expr := 'for' '(' ID '<-' expr ':' expr ')' ':' fat_expr

  while_expr := 'while' '(' expr ')' ':' fat_expr

  func_def := 'function' '(' func_params? ')' ':' fat_expr

  func_params := ID (',' ID)*
```
