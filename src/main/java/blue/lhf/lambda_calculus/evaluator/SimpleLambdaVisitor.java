package blue.lhf.lambda_calculus.evaluator;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

public abstract class SimpleLambdaVisitor implements LambdaVisitor {
    @Override
    public Expression visit(final Abstraction abstraction) {
        return abstraction;
    }

    @Override
    public Expression visit(final Application application) {
        return application;
    }

    @Override
    public Expression visitBodyVariable(final Variable variable) {
        return variable;
    }

    @Override
    public Variable visitLambdaVariable(final Variable variable) {
        return variable;
    }
}
