package blue.lhf.lambda_calculus.parser.tokeniser;

import blue.lhf.lambda_calculus.parser.Lexer;
import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;

@FunctionalInterface
public interface Matcher<V> {
    sealed interface Result<V> permits NoMatch, Match { }
    record NoMatch<V>(String reason) implements Result<V> { }
    record Match<V>(V verifiedIntermediary) implements Result<V> { }

    @NotNull
    Result<V> match(final ListIterator<Lexer.Lexeme> iterator);
}
