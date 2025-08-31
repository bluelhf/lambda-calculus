package blue.lhf.lambda_calculus.parser.tokeniser;

public abstract class SimpleToken implements Token {
    private final String text;

    public SimpleToken(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
