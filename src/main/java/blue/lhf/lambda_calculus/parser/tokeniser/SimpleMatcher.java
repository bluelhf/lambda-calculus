package blue.lhf.lambda_calculus.parser.tokeniser;

import blue.lhf.lambda_calculus.parser.Lexer;
import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;

public class SimpleMatcher implements Matcher<String> {
    private final String text;

    public SimpleMatcher(final String text) {
        this.text = text;
    }

    @Override
    public @NotNull Result<String> match(final ListIterator<Lexer.Lexeme> iterator) {
        final int oldPrevIndex = iterator.previousIndex();
        final StringBuilder builder = new StringBuilder();

        while (iterator.hasNext()) {
            final String lexeme = iterator.next().text();
            builder.append(lexeme);
            if (!text.startsWith(builder.toString())) { // we have a mismatch
                backtrack(iterator, oldPrevIndex + 1);
                return new NoMatch("needed " + text + " but found " + builder);
            }
            if (builder.length() > text.length()) { // found a match but it would split a lexeme
                backtrack(iterator, oldPrevIndex);
                return new NoMatch("needed " + text + " but can only accept at least " + builder);
            }

            if (builder.length() == text.length()) { // found a match
                return new Match<>(builder.toString());
            }
        }

        backtrack(iterator, oldPrevIndex); // we ran out of lexemes before finding a match
        return new NoMatch("ran out of lexemes, only found " + builder + " but needed " + text);
    }

    private void backtrack(final ListIterator<Lexer.Lexeme> iterator, final int targetIndex) {
        while (iterator.previousIndex() >= targetIndex) {
            iterator.previous();
        }
    }
}
