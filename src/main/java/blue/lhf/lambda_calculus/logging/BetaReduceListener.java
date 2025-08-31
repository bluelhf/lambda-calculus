package blue.lhf.lambda_calculus.logging;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Expression;

public interface BetaReduceListener {
    BetaReduceListener NO_OP = (abstraction, argument, reduced) -> {};

    void onBetaReduce(final Abstraction abstraction, final Expression argument, final Expression reduced);
}
