package se.mockachino.verifiers;

public abstract class AbstractVerifier implements Verifier {
	@Override
	public Verifier or(Verifier other) {
		return new OrVerifier(this, other);
	}

	@Override
	public Verifier and(Verifier other) {
		return new AndVerifier(this, other);
	}
}
