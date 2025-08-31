package blue.lhf.lambda_calculus.evaluator;

import blue.lhf.lambda_calculus.logging.BetaReduceListener;
import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

import java.util.concurrent.atomic.AtomicBoolean;

import static blue.lhf.lambda_calculus.logging.BetaReduceListener.NO_OP;

public class Evaluator {
    private final BetaReduceListener listener;

    public Evaluator() {
        this(NO_OP);
    }

    public Evaluator(final BetaReduceListener listener) {
        this.listener = listener;
    }

    public static boolean isReducible(final Expression expression) {
        return switch (expression) {
            case Application application -> (application.function() instanceof Abstraction) || isReducible(application.function()) || isReducible(application.argument());
            case Abstraction abstraction -> isReducible(abstraction.body());
            default -> false;
        };
    }

    /**
     * Rewrites one redux in the expression, the one which is first in depth-first post-order.
     * */
    public Expression reduceOnce(final Expression input) {
        return rewrite(input, new SimpleLambdaVisitor() {
            final AtomicBoolean isReduced = new AtomicBoolean(false);
            @Override
            public Expression visit(final Application application) {
                if (!(application.function() instanceof Abstraction abstraction)) return application;
                if (isReduced.getAndSet(true)) return application;
                return betaReduce(application, abstraction);
            }
        });
    }

    private Expression betaReduce(final Application application, final Abstraction abstraction) {
        final Expression after = rewrite(abstraction.body(), new BetaReducingVisitor(abstraction.variable(), application.argument()));
        Evaluator.this.listener.onBetaReduce(abstraction, application.argument(), after);
        return after;

    }

    /**
     * Re-writes the expression, beta-reducing terms as they are encountered in depth-first post-order.
     * This is not guaranteed to result in an irreducible expression.
     * */
    public Expression reduce(final Expression input) {
        return rewrite(input, new SimpleLambdaVisitor() {
            @Override
            public Expression visit(final Application application) {
                if (!(application.function() instanceof Abstraction abstraction)) return application;
                return betaReduce(application, abstraction);
            }
        });
    }

    public Expression normalise(Expression input) {
        while (isReducible(input)) {
            input = reduce(input);
        }
        return input;
    }

    private Expression rewrite(final Expression input, final LambdaVisitor visitor) {
        return switch (input) {
            case Variable variable -> visitor.visitBodyVariable(variable);
            case Abstraction abstraction -> {
                final Variable visitedVariable = visitor.visitLambdaVariable(abstraction.variable());
                final Expression visitedBody = rewrite(abstraction.body(), visitor);
                yield visitor.visit(new Abstraction() {
                    @Override
                    public Variable variable() {
                        return visitedVariable;
                    }

                    @Override
                    public Expression body() {
                        return visitedBody;
                    }
                });
            }
            case Application application -> {
                final Expression visitedFunction = rewrite(application.function(), visitor);
                final Expression visitedArgument = rewrite(application.argument(), visitor);
                yield visitor.visit(new Application() {
                    @Override
                    public Expression function() {
                        return visitedFunction;
                    }

                    @Override
                    public Expression argument() {
                        return visitedArgument;
                    }
                });
            }
        };
    }
}
