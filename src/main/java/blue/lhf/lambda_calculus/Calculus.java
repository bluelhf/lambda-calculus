package blue.lhf.lambda_calculus;

import blue.lhf.lambda_calculus.evaluator.Evaluator;
import blue.lhf.lambda_calculus.logging.Stringifier;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.parser.Lexer;
import blue.lhf.lambda_calculus.parser.Parser;
import blue.lhf.lambda_calculus.parser.tokeniser.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class Calculus {
	private Calculus() {
	}

	public static void main(final String... args) throws IOException {
		final Lexer lexer = new Lexer();
		final Tokenizer tokenizer = new Tokenizer();
		final Parser parser = new Parser();

		final Evaluator evaluator = new Evaluator((lambda, argument, reduced) -> {
			final String variableLength = (Stringifier.stringify(lambda.variable()) + ":=" + Stringifier.stringify(argument));
			System.out.println("    β-reducing [" + variableLength + "] in: " + Stringifier.stringify(lambda) + " " + Stringifier.stringify(argument));
			final int pad = 22 + Stringifier.stringify(reduced).length() + variableLength.replaceAll("\u001B\\[[;\\d]*m", "").length();
			System.out.printf("%" + pad + "s%n", ("β-reduced form is: " + Stringifier.stringify(reduced)));
			System.out.println();
		});

		final Scanner scanner = new Scanner(System.in);
		System.out.print("> "); System.out.flush();
		while (scanner.hasNext()) {
			eval: {
				final Expression expression = parser.parse(tokenizer.tokenize(lexer.lex(new BufferedReader(new StringReader(scanner.nextLine())))).listIterator());
				if (expression == null) {
					System.out.println("illegal expression");
					break eval;
				}

				System.out.println(Stringifier.stringify(evaluator.normalise(expression)));
			}

			System.out.print("> "); System.out.flush();
		}
	}
}
