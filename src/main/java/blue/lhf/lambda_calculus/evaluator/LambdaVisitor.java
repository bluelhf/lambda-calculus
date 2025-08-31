package blue.lhf.lambda_calculus.evaluator;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

public interface LambdaVisitor {
    Expression visit(final Abstraction abstraction);
    Expression visit(final Application application);
    Expression visitBodyVariable(final Variable variable);
    Variable visitLambdaVariable(final Variable variable);
}
