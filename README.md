# Likely Language

Likely is a language to specify probabilistic models. But not just that! In fact Likely is a full language that will help you to build sequence analysis tools.

It is part of a bigger project, [Likely Framework](https://github.com/igorbonadio/likely), that provides the infrastructure to build and run probabilistic models.

## Quick Start

### Literals

Integer:

```python
1
2
3
```

Floats:

```python
1.1
2.2
3.3
```

Strings:

```python
"Hi"
"Oi"
"Hola"
```

Booleans:

```python
true
false
```

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

### Boolean Expressions

As arithmetic expressions, there isn't operator precedence in boolean expressions:

```python
1 == 1         # => true
1 != 2         # => true
1 > 2          # => false
1 < 2          # => true
2 >= 2         # => true
2 <= 2         # => true
true and true  # => true
true and false # => false
true or true   # => true
true or false  # => true
not true       # => false
```

### Variable, Symbols & Attributions

You don't need to declare variables in Likely. All you need to do is attribute some value to them.

```python
x = 123
```

There is a special use of variables that are different of other common languages. If you don't attribute a value to a variable, its value is a symbol. Symbols are unique in the world, so if you compare _abc == abc_ it will always be true.

```python
abc == abc # => true, if you didn't attribute a value to abc
abc == bcd # => false, if you didn't attribute a value to abc and bcd
```

### Lists

There are 2 ways to define lists. The fat® and thin® way.

Let's start by the fat® way:

```python
colors = (red, blue, black)
```

And the thin® way: _under construction_

```python
colors = (
  red
  blue
  black
)
```

### Pair & Hash

Pairs are useful to define Hash-like data structures:

```python
ages = (igor -> 28, jessica -> 29)
```

or _under construction_

```python
ages = (
  igor    -> 28
  jessica -> 29
)
```

### IF expressions

IF is useful to control the flux of your program.

```python
if (a == b): 1 else: 2
```

Like any other expression, IF always returns a value:

```python
a = 123
b = 321
x = if (a == b): 1 else: 2 # => x == 2
```

There is also a thin® way to define IFs: _under construction_

```python
if (a == b):
  1
else:
  2
```

### FOR expressions

If you need to repeat some steps, use a FOR expression:

```python
for (i in 1:10): x = x + 1
```

And, like any other expression, this for returns a value

```python
x = 0
y = for (i in 1:10): x = x + 1 # => y == 10
```

There is also a thin® way to define FORs: _under construction_

```python
for (i in 1:10):
  x = x + 1
```

### WHILE expressions

WHILE is like a for, but it can run infinitely...

```python
while (true): x = x + 1
```

As you can imagine, WHILE always returns a value. And yes, there is a thin® way to define WHILEs:

```python
while (true):
  x = x + 1
```

### Functions

Functions are one of the most important things in Likely. You can define a function:

```python
sum = function(a, b): a + b
```

And call it:

```python
sum(1, 2)
```

Functions are objects too. So you can send message to them:

```python
sum.apply(1, 2) == sum(1, 2)  # => true
sum.arity                     # => 2
```

You can define functions using the thin® way:

```python
sum = function(a, b):
  a + b
```

If you want, you can use the RETURN statement:

```python
sum = function(a, b):
  return a + b
```

### Constructors

### Probabilities

## Grammar

```
  program := ( NEWLINE | stmt )*

  stmt := expr NEWLINE

  expr := literal | attr | list | pair | comp_expr | binop | boolop | func_call | constructor_call | prob | '(' expr ')'

  literal := ID | number | STRING | boolean

  number := INTEGER | FLOAT

  boolean := 'true' | 'false'

  attr := ID '=' expr

  list := '(' list_body? ')'

  list_body := expr (',' expr)*

  pair := expr '->' expr

  binop := expr op expr

  op := '+' | '-' | '*' | '/' | '==' | '!=' | '>=' | '<=' | '>' | '<' | ':'

  boolop := 'not'? expr ('and' | 'or') expr

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
