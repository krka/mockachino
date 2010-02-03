package se.mockachino.verifier;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.stub.StubStart;

public class VerifyRangeStart implements StubStart {
	private final MockContext mockContext;
	private final int min;
	private final int max;

	public VerifyRangeStart(MockContext mockContext, int min, int max) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
	}

	@Override
	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new Verifier(data, min, max));
	}
}
