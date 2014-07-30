# Likely Language

Likely is a language to specify probabilistic models. But not just that! In fact Likely is a full language that will help you to build sequence analysis tools.

It is part of a bigger project, [Likely Framework](https://github.com/igorbonadio/likely), that provides the infrastructure to build and run probabilistic models.

## Language Reference

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

### Sequences

There are 2 ways to define sequences. The fat® and thin® way.

Let's start by the fat® way:

```python
colors = [red, blue, black]
```

And the thin® way: _under construction_

```python
colors = [
  red
  blue
  black
]
```

### Pair & Hash

Pairs are useful to define Hash-like data structures:

```python
ages = [igor -> 28, jessica -> 29]
```

or _under construction_

```python
ages = [
  igor    -> 28
  jessica -> 29
]
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
for (i <- 1:10): x = x + 1
```

And, like any other expression, this for returns a value

```python
x = 0
y = for (i <- 1:10): x = x + 1 # => y == 10
```

There is also a thin® way to define FORs: _under construction_

```python
for (i <- 1:10):
  x = x + 1
```

### WHILE expressions

WHILE is like a for, but it can run infinitely...

```python
while (true): x = x + 1
```

As you can imagine, WHILE always returns a value. And yes, there is a thin® way to define WHILEs: _under construction_

```python
while (true):
  x = x + 1
```

### Functions

Functions are one of the most important things in Likely. You can define functions:

```python
sum = function(a, b): a + b
mul = function(a, b): { m = a*b; m; }
```

And call them:

```python
sum(1, 2) # => 3
mul(1, 2) # => 2
```

Functions are objects too. So you can send message to them:

```python
sum.apply(1, 2) == sum(1, 2)  # => true
sum.arity                     # => 2
```

You can define and call functions using the thin® way: _under construction_

```python
sum = function(a, b):
  a + b

sum(
  a
  b
)
```

If you want, you can use the RETURN statement:

```python
sum = function(a, b): return a + b
```

### Objects

Everything in Likely is an object. Everything. _under construction_

You can send mensages to an object:

```python
obj.msg(arg1, arg2)
```

And create custom messages:

```python
obj.msg = function(a, b): a + b
```

### Object Templates

An object template defines a constructor function that can create new objects. For example: _under construction_

```python
dog = object(sound):
  bark = function(): "#{sound}!!!"
```
Constructors are like ordinary functions:

```python
rubi = dog("rrrruau")
rubi.bark()  # => "rrrruau!!!"
```

As you saw in the last section, you can add or change object's messages:

```python
rubi = dog("rrrruau")
rubi.bark = function(): "lalala"
rubi.bark()  # => "lalala"
```

If you want to change or add a new message to an object, you can use the constructor's block syntax:

```python
rubi = dog("rrrruau"): { bark = function(): "lalala"; sleep = function(): "zzZZzzZz"; }
rubi.bark()  # => "lalala"
ruby.sleep() # => "zzZZzzZz"

spike = dog("!!!"): sleep = function(): "..."
spike.bark()  # => "!!!"
spike.sleep() # => "..."
```

or

```python
rubi = dog("rrrruau"):
  bark = function(): "lalala"
  sleep = function(): "zzZZzzZz"
rubi.bark()  # => "lalala"
```

### Probabilities

Likely is a language to define probabilistic models. So it includes some syntax to help you write awesome code.

* Discrete Joint Probability Distribution

```python
Prob(a = 0.2, b = 0.8) # ~> Prob(x = a) = 0.2 and Prob(x = b) = 0.8

Prob((a, a) = 0.2, (b, a) = 0.8) # ~> Prob(x = a; y = a) = 0.2 and Prob(x = b; y = a) = 0.8
```

or

```python
Prob(
  a = 0.2 # ~> Prob(x = a) = 0.2
  b = 0.8 # ~> Prob(x = b) = 0.8
)

Prob(
  (a, a) = 0.2 # ~> Prob(x = a; y = a) = 0.2
  (b, a) = 0.8 # ~> Prob(x = b; y = a) = 0.8
)
```

* Discrete Conditional Probability Distribution

```python
Prob(a | a = 0.2, b | a = 0.8, a | b = 0.6, b | b = 0.4) # ~> Prob(x_i = a | x_{i-1} = a) = 0.2, Prob(x_i = b | x_{i-1} = a) = 0.8, Prob(x_i = a | x_{i-1} = b) = 0.4 and Prob(x_i = b | x_{i-1} = b) = 0.6

Prob((a, a) | (a, b) = 0.2, (b, a) | (a, b) = 0.8, (a, a) | (b, b) = 0.6, (b, a) | (b, b) = 0.4) # ~> Prob(x_i = a; y_i = a | x_{i-1} = a; y_{i-1} = b) = 0.2, Prob(x_i = b; y_i = a | x_{i-1} = a; y_{i-1} = b) = 0.8, Prob(x_i = a; y_i = a | x_{i-1} = b; y_{i-1} = b) = 0.4 and Prob(x_i = b; y_i = a | x_{i-1} = b; y_{i-1} = b) = 0.6
```

or

```python
Prob(
  a | a = 0.2 # ~> Prob(x_i = a | x_{i-1} = a) = 0.2
  b | a = 0.8 # ~> Prob(x_i = b | x_{i-1} = a) = 0.8
  a | b = 0.6 # ~> Prob(x_i = a | x_{i-1} = b) = 0.4
  b | b = 0.4 # ~> Prob(x_i = b | x_{i-1} = b) = 0.6
)

Prob(
  (a, a) | (a, b) = 0.2 # ~> Prob(x_i = a; y_i = a | x_{i-1} = a; y_{i-1} = b) = 0.2
  (b, a) | (a, b) = 0.8 # ~> Prob(x_i = b; y_i = a | x_{i-1} = a; y_{i-1} = b) = 0.8
  (a, a) | (b, b) = 0.6 # ~> Prob(x_i = a; y_i = a | x_{i-1} = b; y_{i-1} = b) = 0.4
  (b, a) | (b, b) = 0.4 # ~> Prob(x_i = b; y_i = a | x_{i-1} = b; y_{i-1} = b) = 0.6
)
```

## Grammar

```
  program := ( NEWLINE | stmt )*

  stmt := expr (NEWLINE | ';')

  expr := literal | obj_msg | attr | seq | pair | comp_expr | binop | boolop | func_call | constructor_call | prob | return_expr | '(' expr ')'

  literal := ID | number | STRING | boolean

  number := INTEGER | FLOAT

  boolean := 'true' | 'false'

  obj_msg := ID '.' ID

  attr := (ID | obj_msg) '=' expr

  seq := '[' seq_body? ']'

  seq_body := expr (',' expr)*

  pair := expr '->' expr

  binop := expr op expr

  op := '+' | '-' | '*' | '/' | '==' | '!=' | '>=' | '<=' | '>' | '<' | ':'

  boolop := 'not'? expr ('and' | 'or') expr

  return_expr := 'return' expr

  func_call := func list

  list := '(' list_body? ')'

  list_body := expr (',' expr)*

  func := ID | '(' expr ')'

  constructor_call := func_call ':' fat_expr

  fat_expr := expr | '{' (expr ';') '}'

  prob := prob_vars '=' number

  prob_var := joint_vars | cond_vars

  joint_vars := ID | list

  cond_vars := joint_vars '|' joint_vars

  comp_expr := if_expr | for_expr | while_expr | func_def

  if_expr := 'if' '(' expr ')' ':' fat_expr 'else' ':' fat_expr

  for_expr := 'for' '(' ID '<-' expr ':' expr ')' ':' fat_expr

  while_expr := 'while' '(' expr ')' ':' fat_expr

  func_def := 'function' '(' func_params? ')' ':' fat_expr

  func_params := ID (',' ID)*
```
