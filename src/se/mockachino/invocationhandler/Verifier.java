package se.mockachino.invocationhandler;

import se.mockachino.invocationhandler.VerificationHandler;
import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;

public class Verifier<T> extends VerificationHandler {
	private MockData<T> mockData;
	private final int minCalls;
	private final int maxCalls;

	public Verifier(MockData<T> mockData, int minCalls, int maxCalls) {
		super("Verifier");
		this.mockData = mockData;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
	}

	@Override
	public void verify(Object o, MethodMatcher matcher) {
		int counter = 0;
		for (MethodCall call : mockData.getCalls()) {
			if (matcher.matches(call)) {
				counter++;
			}
		}
		if (counter < minCalls) {
			error(String.format("Expected %d call(s) to %s but only got %d call(s)", minCalls, matcher.toString(), counter), matcher);
		}
		if (counter > maxCalls) {
			error(String.format("Expected %d call(s) to %s but got %d call(s)", maxCalls, matcher.toString(), counter), matcher);
		}
	}

	private void error(String msg, MethodMatcher matcher) {
		String matchingMethods = getBestMatches(matcher);
		throw new VerificationError(msg + matchingMethods);
	}

	private String getBestMatches(MethodMatcher matcher) {
		String matchingMethods = "";
		for (MethodCall call : mockData.getCalls()) {
			if (call.getMethod().equals(matcher.getMethod())) {
				matchingMethods += "\nActual method called: " + call;
			}
		}
		return matchingMethods;
	}
}
