package blue.lhf.lambda_calculus.parser;

import blue.lhf.lambda_calculus.model.Abstraction;
import blue.lhf.lambda_calculus.model.Application;
import blue.lhf.lambda_calculus.model.Expression;
import blue.lhf.lambda_calculus.model.Variable;
import blue.lhf.lambda_calculus.parser.tokeniser.Token;
import blue.lhf.lambda_calculus.parser.tokeniser.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Parser {
	private static boolean skipToNextSubExpression(ListIterator<Token> tokens) {
		if (tokens.hasNext()) {
			final Token token = tokens.next();
			if (token instanceof Space) {
				return true;
			}

			// These tokens may be future sub-expressions for application
			if (token instanceof LeftParen || token instanceof VariableToken || token instanceof Lambda) {
				tokens.previous();
				return true;
			}

			tokens.previous();
			return false;
		}
		return false;
	}

	public Expression parse(final ListIterator<Token> tokens) {
		final Expression expr = parseExpression(tokens, new HashMap<>());
		if (tokens.hasNext()) {
			return null;
		}

		return expr;
	}

    private Expression parseExpression(final ListIterator<Token> tokens, final Map<String, Variable> boundVariables) {
        if (!tokens.hasNext()) return null;
        final List<Expression> expressions = new ArrayList<>();
        do {
            switch (tokens.next()) {
                case Tokenizer.LeftParen ignored -> {
                    final Expression inner = parseExpression(tokens, boundVariables);
                    if (!tokens.hasNext() || !(tokens.next() instanceof Tokenizer.RightParen)) {
                        return null;
                    }
                    expressions.add(inner);
                }
                case Tokenizer.Lambda ignored -> {
                    final List<Tokenizer.VariableToken> variableTokens = new ArrayList<>();
                    while (tokens.hasNext() && (tokens.next() instanceof Tokenizer.VariableToken v)) {
                        variableTokens.add(v);
                    }
                    tokens.previous();
                    if (!tokens.hasNext() || !(tokens.next() instanceof Tokenizer.Dot)) {
                        tokens.previous();
                        return null;
                    }
                    final Map<String, Variable> newBoundVariables = new HashMap<>(Map.copyOf(boundVariables));
                    final List<Variable> lambdaArguments = new ArrayList<>();
                    for (Tokenizer.VariableToken variableToken : variableTokens) {
                        final String variableName = variableToken.getText();

                        final Variable variable = new Variable() {
                            @Override
                            public String toString() {
                                return variableName;
                            }
                        };

                        lambdaArguments.add(variable);
                        newBoundVariables.put(variableName, variable); // shadow pre-existing bound variables
                    }
                    final Expression body = parseExpression(tokens, newBoundVariables);
                    if (body == null) return null;

                    final ListIterator<Variable> argumentIterator = lambdaArguments.listIterator(lambdaArguments.size());
                    Expression inner = body;

                    do {
                        final Expression currentInner = inner;
                        final Variable innerVariable = argumentIterator.previous();
                        argumentIterator.remove();
                        inner = new Abstraction() {
                            @Override
                            public Variable variable() {
                                return innerVariable;
                            }

                            @Override
                            public Expression body() {
                                return currentInner;
                            }
                        };
                    } while (argumentIterator.hasPrevious());
                    expressions.add(inner);
                }
                case Tokenizer.VariableToken variable -> {
                    expressions.add(boundVariables.computeIfAbsent(variable.getText(), name -> new Variable() {
                        @Override
                        public String toString() {
                            return name;
                        }
                    }));
                }
                default -> {
                    tokens.previous();
                    return null;
                }
            }
        } while (consumeIfSpace(tokens));

        if (expressions.size() > 1) {
            Application result = new Application() {
                @Override
                public Expression function() {
                    return expressions.getFirst();
                }

                @Override
                public Expression argument() {
                    return expressions.get(1);
                }
            };

            for (int i = 2; i < expressions.size(); i++) {
                final Expression currentResult = result;
                final Expression childExpression = expressions.get(i);
                result = new Application() {
                    @Override
                    public Expression function() {
                        return currentResult;
                    }

                    @Override
                    public Expression argument() {
                        return childExpression;
                    }
                };
            }

            return result;
        }
        return expressions.getFirst();
    }

}
