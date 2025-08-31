package blue.lhf.lambda_calculus.parser.tokeniser;

import blue.lhf.lambda_calculus.parser.Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Tokenizer {
    public static final SimpleTokenChain<LeftParen> LEFT_PAREN_CHAIN = SimpleTokenChain.of("(", LeftParen::new);
    public static final SimpleTokenChain<RightParen> RIGHT_PAREN_CHAIN = SimpleTokenChain.of(")", RightParen::new);
    public static final SimpleTokenChain<Dot> DOT_CHAIN = SimpleTokenChain.of(".", Dot::new);
    public static final SimpleTokenChain<Lambda> LAMBDA_CHAIN = SimpleTokenChain.of("λ", Lambda::new);
    public static final SimpleTokenChain<Space> SPACE_CHAIN = SimpleTokenChain.of(" ", Space::new);

    public static final TokenChain<Matcher<Lexer.Lexeme>, Lexer.Lexeme, Builder<Lexer.Lexeme, VariableToken>, VariableToken> VARIABLE_CHAIN = TokenChain.of(
            (lexemes) -> new Matcher.Match<>(lexemes.next()),
            lexeme -> new VariableToken(lexeme.text())
    );

    private static final List<TokenChain<?, ?, ?, ?>> TOKEN_CHAINS = List.of(
            LEFT_PAREN_CHAIN, RIGHT_PAREN_CHAIN, LAMBDA_CHAIN, SPACE_CHAIN, DOT_CHAIN, VARIABLE_CHAIN
    );

    public final List<Token> tokenize(final List<Lexer.Lexeme> lexemes) {
        final ListIterator<Lexer.Lexeme> iterator = lexemes.listIterator();
        final List<Token> tokens = new ArrayList<>();
        while (iterator.hasNext()) {
            boolean found = false;
            for (final TokenChain<?, ?, ?, ?> chain : TOKEN_CHAINS) {
                final Token token = chain.tokenise(iterator);
                if (token != null) {
                    tokens.add(token);
                    found = true;
                    break;
                }
            }
            if (found) continue;
            throw new IllegalArgumentException("Unrecognised lexeme " + iterator.next());
        }

        return tokens;
    }


    public static class LeftParen extends SimpleToken {
        public LeftParen(final String text) {
            super(text);
        }
    }

    public static class RightParen extends SimpleToken {
        public RightParen(String text) {
            super(text);
        }
    }

    public static class Lambda extends SimpleToken {
        public Lambda(String text) {
            super(text);
        }
    }

    public static class Space extends SimpleToken {
        public Space(String text) {
            super(text);
        }
    }

    public static class VariableToken extends SimpleToken {
        public VariableToken(String text) {
            super(text);
        }
    }

    public static class Dot extends SimpleToken {
        public Dot(String text) {
            super(text);
        }
    }
}