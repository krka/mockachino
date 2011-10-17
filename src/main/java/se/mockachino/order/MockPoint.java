package se.mockachino.order;

public class MockPoint {
	private final int callNumber;

	public MockPoint(int callNumber) {
		this.callNumber = callNumber;
	}

	public int getCallNumber() {
		return callNumber;
	}

	@Override
	public String toString() {
		return "MockPoint[" + callNumber + "]";
	}
}
