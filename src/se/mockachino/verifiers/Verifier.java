package se.mockachino.verifiers;

import se.mockachino.MethodCall;

import java.util.List;

public interface Verifier {
	List<MethodCall> getMatches();

	Verifier or(Verifier other);
	Verifier and(Verifier other);
}
