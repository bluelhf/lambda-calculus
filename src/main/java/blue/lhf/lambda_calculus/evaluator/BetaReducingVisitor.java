package blue.lhf.lambda_calculus.evaluator;

import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

public class BetaReducingVisitor extends SimpleLambdaVisitor {
    private final Variable term;
    private final Expression reduction;

    public BetaReducingVisitor(final Variable term, final Expression reduction) {
        this.term = term;
        this.reduction = reduction;
    }

    @Override
    public Expression visitBodyVariable(final Variable variable) {
        return variable == term ? reduction : variable;
    }
}
