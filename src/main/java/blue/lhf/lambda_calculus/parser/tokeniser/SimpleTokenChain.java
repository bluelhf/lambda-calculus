package blue.lhf.lambda_calculus.parser.tokeniser;

import blue.lhf.lambda_calculus.parser.Lexer.Lexeme;

import java.util.ListIterator;
import java.util.function.Function;

public abstract class SimpleTokenChain<T extends Token> implements TokenChain<SimpleMatcher, String, Builder<String, T>, T> {
    private final SimpleMatcher matcher;

    public SimpleTokenChain(final String text) {
        this.matcher = new SimpleMatcher(text);
    }

    public static <T extends Token> SimpleTokenChain<T> of(final String text, final Function<String, T> builder) {
        return new SimpleTokenChain<T>(text) {
            @Override
            protected T build(String text) {
                return builder.apply(text);
            }
        };
    }

    protected abstract T build(final String text);

    @Override
    public T tokenise(ListIterator<Lexeme> iterator) {
        return switch (matcher.match(iterator)) {
            case Matcher.NoMatch<String> ignored -> null;
            case Matcher.Match<String> m -> build(m.verifiedIntermediary());
        };
    }
}
