package blue.lhf.lambda_calculus.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public record Lexeme(String text) {}
    public List<Lexeme> lex(final BufferedReader input) throws IOException {
        final List<Lexeme> lexemes = new ArrayList<>();

        final StringBuilder nameBuilder = new StringBuilder();
        int read;
        while ((read = input.read()) != -1) {
            switch (read) {
                case '(', ')', 'λ', ' ', '.' -> {
                    if (!nameBuilder.isEmpty()) {
                        lexemes.add(new Lexeme(nameBuilder.toString()));
                        nameBuilder.setLength(0);
                    }

                    lexemes.add(new Lexeme(Character.toString(read)));
                }
                default -> {
                    if (nameBuilder.isEmpty() || read == '′') {
                        nameBuilder.append(Character.toString(read));
                        continue;
                    }
                    lexemes.add(new Lexeme(nameBuilder.toString()));
                    nameBuilder.setLength(0);
                    nameBuilder.append(Character.toString(read));
                }
            }
        }

        if (!nameBuilder.isEmpty()) {
            lexemes.add(new Lexeme(nameBuilder.toString()));
            nameBuilder.setLength(0);
        }

        return lexemes;
    }
}
