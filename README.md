# Lambda Calculus in Java

This project implements a lexer, tokenizer, LR parser, and evaluator for the Lambda calculus in Java.

## Features

### Simplified reduction model
- Evaluation is done correctly **without α- or η-reduction**.
    - η-reduction is treated simply as a special case of β-reduction.
    - Evaluation without α-reduction is achieved by separating the identity of a variable from its name. A variable's identity can be specified:  
      1. by providing the model of a calculation directly in Java (variables are identified by reference equality),
      2. automatically by the parser, where variables in the same scope are reference equal in the model. Shadowed and free variables are supported.
      3. explicitly, by providing α-reduced expressions to the parser, differentiating namesakes using the prime character (′).

### Minimal calculus model
- The entire calculus is defined by four interfaces: `Expression`, `Application`, `Abstraction`, and `Variable`, each with no logic whatsoever.
- β-reduction is implemented using a visitor that traverses the expression left-recursively.

### Correct and feature-rich parsing
- Outer parentheses may be omitted: `λx.y => (λx.y)`
- The two terms of an application need not be separated by a space: `ab = a b`
- Repeated application is left-associative: `fxf => (fx)f`
- Curried lambdas are supported: `λxy.x => λx.λy.x`
- Lambda bodies are greedy: `λx.x y z => λx.(x y z)`
- Variables are defined using _any single character_
  - A variable name may also include any number of prime characters (′) after the name.

### API support for Church numbers and Boolean logic
- `blue.lhf.lambda_calculus.Library` contains utility methods for generating Church numbers, calculus expressions and Boolean values and connectives directly as models instead of parsing.

### Simple REPL with β-reduction steps and colour-coded variable identities
