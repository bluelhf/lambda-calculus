package blue.lhf.lambda_calculus.parser.tokeniser;

@FunctionalInterface
public interface Builder<V, T> {
    T build(final V verifiedIntermediary);
}
