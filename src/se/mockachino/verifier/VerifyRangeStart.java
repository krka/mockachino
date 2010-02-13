package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.order.MockPoint;

public class VerifyRangeStart {
	private final MockContext mockContext;
	private final int min;
	private final int max;
	private final MockPoint start;
	private final MockPoint end;

	public VerifyRangeStart(MockContext mockContext, int min, int max) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
		start = mockContext.BIG_BANG;
		end = mockContext.BIG_CRUNCH;
	}

	public VerifyRangeStart(MockContext mockContext, int min, int max, MockPoint start, MockPoint end) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
		this.start = start;
		this.end = end;
	}


	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		Iterable<MethodCall> calls = data.getCalls(start, end);
		return mockContext.createProxy(mock, new VerifyHandler(mock, calls, min, max));
	}
}
