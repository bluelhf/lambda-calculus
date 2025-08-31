package blue.lhf.lambda_calculus.parser.tokeniser;

import blue.lhf.lambda_calculus.parser.Lexer.Lexeme;

import java.util.ListIterator;

public interface TokenChain<M extends Matcher<V>, V, B extends Builder<V, T>, T extends Token> {
    T tokenise(final ListIterator<Lexeme> iterator);

    static <M extends Matcher<V>, V, B extends Builder<V, T>, T extends Token> TokenChain<M, V, B, T> of(final Matcher<V> matcher, final Builder<V, T> builder) {
        return iterator -> switch (matcher.match(iterator)) {
            case Matcher.NoMatch<V> ignored -> null;
            case Matcher.Match<V> m -> builder.build(m.verifiedIntermediary());
        };
    }
}
