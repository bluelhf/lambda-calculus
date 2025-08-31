package blue.lhf.lambda_calculus.logging;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;

import java.awt.*;

import static java.lang.System.identityHashCode;

public class Stringifier {
    public static String stringify(Expression expression) {
        final StringBuilder builder = new StringBuilder();
        switch (expression) {
            case Abstraction abstraction -> {
                builder.append("(λ");
                builder.append(stringify(abstraction.variable()));
                builder.append(".").append(stringify(abstraction.body())).append(")");
            }
            case Application application -> {
                builder.append("(");
                int count = 0;
                Expression current = application;
                while (current instanceof Application sub && sub.function() == application.function()) {
                    current = sub.argument();
                    count++;
                }
                builder.append(stringify(application.function()));
                if (count > 1) {
                    final StringBuilder subBuilder = new StringBuilder();
                    while (count > 0) {
                        switch (count % 10) {
                            case 1 -> subBuilder.append("¹");
                            case 2, 3 -> subBuilder.append(Character.toChars(176 + count % 10));
                            default -> subBuilder.append(Character.toChars(8304 + count % 10));
                        }
                        count /= 10;
                    }
                    builder.append(subBuilder.reverse());
                    builder.append(" ").append(stringify(current));
                } else {
                    builder.append(" ").append(stringify(application.argument()));
                }
                builder.append(")");
            }
            case Variable variable -> {
                final Color colour = Color.getHSBColor(System.identityHashCode(variable) % 1000 * 0.1f, 1f, 1f);
                builder.append("\u001B[38;2;%d;%d;%dm%s\u001B[0m".formatted(colour.getRed(), colour.getGreen(), colour.getBlue(), variable));
            }
        }
        return builder.toString();
    }

    private static String mermaidRows(final Expression expression) {
        switch (expression) {
            case Variable variable -> {
                return identityHashCode(expression) + "[\"Variable\n" + identityHashCode(expression) + " (" + variable + ")\"]";
            }

            case Abstraction abstraction -> {
                return identityHashCode(expression) + "[Abstraction]" + "\n"
                        + identityHashCode(expression) + " --body--> " + identityHashCode(abstraction.body()) + "\n"
                        + identityHashCode(expression) + " --var--> " + identityHashCode(abstraction.variable()) + "\n"
                        + mermaidRows(abstraction.variable()) + "\n"
                        + mermaidRows(abstraction.body());
            }

            case Application application -> {
                return identityHashCode(expression) + "[Application]" + "\n"
                        + identityHashCode(expression) + " --function--> " + identityHashCode(application.function()) + "\n"
                        + identityHashCode(expression) + " --argument--> " + identityHashCode(application.argument()) + "\n"
                        + mermaidRows(application.function()) + "\n"
                        + mermaidRows(application.argument());
            }
        }
    }

    public static String stringifyMermaid(Expression expression) {
        return "graph TD\n" + mermaidRows(expression).indent(2);
    }
}
