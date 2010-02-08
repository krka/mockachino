package se.mockachino.verifier;

import se.mockachino.MockContext;
import se.mockachino.MockData;

public class VerifyRangeStart {
	private final MockContext mockContext;
	private final int min;
	private final int max;

	public VerifyRangeStart(MockContext mockContext, int min, int max) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new VerifyHandler(mock, data, min, max));
	}
}
