package blue.lhf.lambda_calculus.model;

public non-sealed interface Application extends Expression {
    Expression function();
    Expression argument();
}
