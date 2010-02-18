package se.mockachino.stub;

import se.mockachino.util.MockachinoMethod;

public class AcceptAllVerifier implements StubVerifier {
	public static final StubVerifier INSTANCE = new AcceptAllVerifier();

	@Override
	public void verify(MockachinoMethod method) {
	}
}
