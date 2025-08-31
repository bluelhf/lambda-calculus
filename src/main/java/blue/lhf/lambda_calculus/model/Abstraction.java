package blue.lhf.lambda_calculus.model;

public non-sealed interface Abstraction extends Expression {
    Variable variable();
    Expression body();
}
