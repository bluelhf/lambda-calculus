package blue.lhf.lambda_calculus;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

import java.util.HashSet;
import java.util.Set;

public class Library {
    public static Variable var(final String name) {
        return new Variable() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

    record AbstractionImpl(Variable variable, Expression body) implements Abstraction {}
    public static Abstraction abstraction(final Variable variable, final Expression body) {
        return new AbstractionImpl(variable, body);
    }

    record ApplicationImpl(Expression function, Expression argument) implements Application {}
    public static Application application(final Expression function, final Expression argument) {
        return new ApplicationImpl(function, argument);
    }

    public static Expression churchNumeral(final int number) {
        final Variable x = var("x");
        final Variable f = var("f");

        Expression result = x;
        for (int count = 0; count < number; count++) {
            final Expression inner = result;
            result = application(f, inner);
        }

        return abstraction(f, abstraction(x, result));
    }

    public static Expression exprSum() {
        final Variable m = var("m");
        final Variable n = var("n");
        final Variable f = var("f");
        final Variable x = var("x");

        return abstraction(
                m,
                abstraction(
                        n,
                        abstraction(
                                f,
                                abstraction(
                                        x,
                                        application(
                                                application(m, f),
                                                application(
                                                        application(n, f),
                                                        x
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static Expression exprSucc() {
        final Variable n = var("n");
        final Variable f = var("f");
        final Variable x = var("x");

        return abstraction(
                n, abstraction(
                        f, abstraction(
                                x, application(
                                        f, application(
                                                application(n, f),
                                                x
                                        )
                                )
                        )
                )
        );
    }

    public static Abstraction identity(final Variable variable) {
        return new Abstraction() {
            @Override public Variable variable() { return variable; }
            @Override public Expression body() { return variable; }
        };
    }

    public static boolean isClosed(final Expression expression) {
        return switch (expression) {
            case Variable ignored -> true;
            case Application application -> isClosed(application.function()) && isClosed(application.argument());
            case Abstraction abstraction -> isClosed(abstraction.body()) || FV(abstraction).isEmpty();
        };
    }

    public static Set<Variable> FV(final Expression expression) {
        return switch (expression) {
            case Variable variable -> Set.of(variable);
            case Application application -> {
                final Set<Variable> free = new HashSet<>();
                free.addAll(FV(application.function()));
                free.addAll(FV(application.argument()));
                yield free;
            }
            case Abstraction abstraction -> {
                final Set<Variable> free = new HashSet<>(FV(abstraction.body()));
                free.remove(abstraction.variable());
                yield free;
            }
        };
    }


}
