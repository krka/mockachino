package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.MockData;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.Formatting;
import se.mockachino.util.MockachinoMethod;

import java.util.ArrayList;
import java.util.Collections;
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
				expected = "Expected " + Formatting.calls(minCalls);
			} else {
				expected = "Expected at least " + Formatting.calls(minCalls);
			}
			error(expected + ", but got " + Formatting.calls(counter), matcher, 3);
		}
		if (counter > maxCalls) {
			String expected;
			if (exact) {
				expected = "Expected " + Formatting.calls(maxCalls);
			} else {
				expected = "Expected at most " + Formatting.calls(maxCalls);
			}
			error(expected + ", but got " + Formatting.calls(counter), matcher, 0);
		}
	}

	private void error(String msg, MethodMatcher matcher, int maxMisses) {
		String matchingMethods = getBestMatches(matcher, maxMisses);
		throw new VerificationError(msg + matchingMethods);
	}

	private List<MethodCall> getSortedMatches(MethodMatcher matcher) {
		List<MethodCall> list = new ArrayList<MethodCall>(mockData.getCalls());
		Collections.sort(list, new MethodComparator(matcher));
		return list;
	}

	private String getBestMatches(MethodMatcher matcher, int maxMisses) {
		List<MethodCallCount> calls = getMethodCallCount(getSortedMatches(matcher));
		List<MethodCallCount> toOutput = new ArrayList<MethodCallCount>();

		int numMisses = 0;

		for (MethodCallCount call : calls) {
			boolean hit = matcher.matches(call);
			if (!hit) {
				numMisses += 1;
			}
			if (hit || numMisses <= maxMisses) {
				toOutput.add(call);
			}
		}
		String expected = "Method pattern:\n"  + matcher.toString();
		String matchingMethods = "\n" + expected + "\n";

		boolean reachedHit = false;
		boolean reachedMiss = false;
		for (MethodCallCount call : toOutput) {
			boolean hit = matcher.matches(call);
			if (hit && !reachedHit) {
				matchingMethods += "Hits:\n";
				reachedHit = true;
			}
			if (!hit && !reachedMiss) {
				matchingMethods += "Misses:\n";
				reachedMiss = true;
			}

			matchingMethods += call.toString() + count(call) + "\n" + getStacktrace(call);
		}
		if (maxMisses < numMisses) {
			matchingMethods += "... skipping " + (numMisses - maxMisses) + " unmatched calls\n";
		}
		return matchingMethods;
	}

	private String count(MethodCallCount call) {
		if (call.count <= 1) {
			return "";
		}
		return "\t\t[invoked " + call.count + " times]";
	}

	private List<MethodCallCount> getMethodCallCount(List<MethodCall> calls) {
		List<MethodCallCount> res = new ArrayList<MethodCallCount>();

		MethodCall prev = null;
		MethodCallCount current = null;
		for (MethodCall call : calls) {
			if (call.equals(prev)) {
				current.count++;
			} else {
				if (current != null) {
					res.add(current);
				}
				current = new MethodCallCount(
						call.getMethod(),
						call.getArguments(),
						call.getCallNumber(),
						call.getStackTrace());
				prev = call;
			}
		}
		if (current != null) {
			res.add(current);
		}
		return res;
	}

	private String getStacktrace(MethodCall call) {
		String traceString = call.getStackTraceString(1);
		if (traceString == null) {
			return "";
		}
		return traceString;
	}

	private static class MethodCallCount extends MethodCall {
		int count = 1;
		public MethodCallCount(MockachinoMethod method, Object[] args, int callNumber, StackTraceElement[] stacktrace) {
			super(method, args, callNumber, stacktrace);
		}
	}
}
