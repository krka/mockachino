package se.mockachino.stub;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class MethodStubs<T> {
	private final List<MethodStub<T>> stubs;

	public MethodStubs() {
		stubs = new ArrayList<MethodStub<T>>();
	}

	public synchronized MethodStub<T> findMatch(MethodCall<T> call) {
		int n = stubs.size();
		for (int i = n - 1; i >= 0; i--) {
			MethodStub<T> stub = stubs.get(i);
			if (stub.getMatcher().matches(call)) {
				return stub;
			}
		}
		return null;
	}

	public synchronized void add(MethodStub<T> stub) {
		stubs.add(stub);
	}

	public void clear() {
		stubs.clear();
	}
}
