package se.mockachino.order;

import se.mockachino.MockContext;

public class MockPoint {
	private final MockContext context;
	private final int callNumber;

	public MockPoint(MockContext context, int callNumber) {
		this.context = context;
		this.callNumber = callNumber;
	}

	public MockContext getMockContext() {
		return context;
	}

	public int getCallNumber() {
		return callNumber;
	}
}
