package se.mockachino.stub;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class MethodStubs {
	private final List<MethodStub> stubs;

	public MethodStubs() {
		stubs = new ArrayList<MethodStub>();
	}

	public synchronized MethodStub findMatch(MethodCall call) {
		int n = stubs.size();
		for (int i = n - 1; i >= 0; i--) {
			MethodStub stub = stubs.get(i);
			if (stub.getMatcher().matches(call)) {
				return stub;
			}
		}
		return null;
	}

	public synchronized void add(MethodStub stub) {
		stubs.add(stub);
	}

	public void clear() {
		stubs.clear();
	}
}
