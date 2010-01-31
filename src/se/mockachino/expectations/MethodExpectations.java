package se.mockachino.expectations;

import se.mockachino.MethodCall;

import java.util.List;
import java.util.ArrayList;

public class MethodExpectations {
	private final List<MethodExpectation> expectations;
	//private final MethodExpectation defaultExpectation;

	public MethodExpectations(Class<?> returnType) {
		expectations = new ArrayList<MethodExpectation>();
		//defaultExpectation = DefaultMethodExpectations.forType(returnType);
	}


	public MethodExpectation findMatch(MethodCall call) {
		int n = expectations.size();
		for (int i = n - 1; i >= 0; i--) {
			MethodExpectation expectation = expectations.get(i);
			if (expectation.matches(call)) {
				return expectation;
			}
		}
		return null;
	}

	public void add(MethodExpectation expectation) {
		expectations.add(expectation);
	}
}
