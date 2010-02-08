package se.mockachino.verifier;

import se.mockachino.util.Formatting;
import se.mockachino.verifier.MatchingHandler;
import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;

import java.util.List;

public class VerifyHandler<T> extends MatchingHandler {
	private final MockData<T> mockData;
	private final int minCalls;
	private final int maxCalls;

	public VerifyHandler(T mock, MockData<T> mockData, int minCalls, int maxCalls) {
		super("VerifyHandler", mock.toString());
		this.mockData = mockData;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
	}

	@Override
	public void match(Object o, MethodMatcher matcher) {
		int counter = 0;
		List<MethodCall> calls = mockData.getCalls();
		for (MethodCall call : calls) {
			if (matcher.matches(call)) {
				counter++;
			}
		}
		boolean exact = minCalls == maxCalls;
		if (counter < minCalls) {
			String expected;
			if (exact) {
				expected = "Expected exactly " + Formatting.calls(minCalls);
			} else {
				expected = "Expected at least " + Formatting.calls(minCalls);
			}
			error(expected + ", but got " + Formatting.calls(counter), matcher);
		}
		if (counter > maxCalls) {
			String expected;
			if (exact) {
				expected = "Expected exactly " + Formatting.calls(maxCalls);
			} else {
				expected = "Expected at most" + Formatting.calls(maxCalls);
			}
			error(expected + ", but got " + Formatting.calls(counter), matcher);
		}
	}

	private void error(String msg, MethodMatcher matcher) {
		String matchingMethods = getBestMatches(matcher);
		String expected = "EXPECTED:     mock." + matcher.toString();
		throw new VerificationError(msg + "\n" + matchingMethods + expected);
	}

	private String getBestMatches(MethodMatcher matcher) {
		String matchingMethods = "";
		List<MethodCall> calls = mockData.getCalls();
		for (MethodCall call : calls) {
			if (call.getMethod().equals(matcher.getMethod())) {
				String prefix = (matcher.matches(call)) ? "ACTUAL: (HIT) " : "ACTUAL:       ";
				matchingMethods += prefix + "mock." + call + "\n" + getStacktrace(call);
			}
		}
		return matchingMethods;
	}

	private String getStacktrace(MethodCall call) {
		return "";
		//return call.getStackTrace();
	}
}
