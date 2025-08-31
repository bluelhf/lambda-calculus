package blue.lhf.lambda_calculus;

import blue.lhf.lambda_calculus.evaluator.Evaluator;
import blue.lhf.lambda_calculus.logging.Stringifier;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.parser.Lexer;
import blue.lhf.lambda_calculus.parser.Parser;
import blue.lhf.lambda_calculus.parser.tokeniser.Token;
import blue.lhf.lambda_calculus.parser.tokeniser.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static blue.lhf.lambda_calculus.Library.*;
import static blue.lhf.lambda_calculus.evaluator.Evaluator.isReducible;

public class Calculus {
    private Calculus() {
    }

    public static void main(final String... args) throws IOException {
        final List<Token> tokens = new Tokenizer().tokenize(new Lexer().lex(new BufferedReader(new StringReader(
                "(((λm.(λn.(λf.(λx.((m f) ((n f) x)))))) (λf.(λx.(f (f (f (f (f (f x))))))))) (λf.(λx.(f x))))"
        ))));

        final Expression expression = new Parser().parse(tokens.listIterator());
        final Evaluator evaluator = new Evaluator((lambda, argument, reduced) -> {
            final String variableLength = (Stringifier.stringify(lambda.variable()) + ":=" + Stringifier.stringify(argument));
            System.out.println("    β-reducing [" + variableLength + "] in: " + Stringifier.stringify(lambda) + " " + Stringifier.stringify(argument));
            final int pad = 22 + Stringifier.stringify(reduced).length() + variableLength.replaceAll("\u001B\\[[;\\d]*m", "").length();
            System.out.printf("%" + pad + "s%n", ("β-reduced form is: " + Stringifier.stringify(reduced)));
            System.out.println();
        });

        evaluator.normalise(expression);

        Expression expr = application(application(exprSum(), churchNumeral(6)), churchNumeral(1));
        while (isReducible(expr)) {
            expr = evaluator.reduceOnce(expr);
        }
    }
}
