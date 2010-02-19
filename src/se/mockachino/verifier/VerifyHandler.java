package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class VerifyHandler<T> extends MatchingHandler {
	private final Iterable<MethodCall> calls;
	private final int minCalls;
	private final int maxCalls;

	public VerifyHandler(T mock, Iterable<MethodCall> calls,
						 int minCalls, int maxCalls) {
		super("VerifyHandler", mock.toString());
		this.calls = calls;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		int counter = 0;
		for (MethodCall call : calls) {
			if (matcher.matches(call)) {
				counter++;
			}
		}
		String errorMessage = new Reporter(counter, minCalls, maxCalls).getErrorLine();
		if (errorMessage != null) {
			boolean wantsMisses = minCalls > 0;
			error(errorMessage, matcher, wantsMisses ? 3 : 0);
		}
	}

	private void error(String msg, MethodMatcher matcher, int maxMisses) {
		MethodCallGrouper grouper = new MethodCallGrouper(matcher, calls);
		grouper.getGroupedCalls();
		String matchingMethods = getBestMatches(matcher, maxMisses, grouper);
		throw new VerificationError(msg + matchingMethods);
	}

	private String getBestMatches(MethodMatcher matcher, int maxMisses, MethodCallGrouper grouper) {
		StringBuilder report = new StringBuilder();
		report.append("\nMethod pattern:\n"  + matcher.toString() + "\n");

		boolean reachedHit = false;
		boolean reachedMiss = false;
		List<MethodCallCount> calls = grouper.getGroupedCalls();
		List<MethodCallCount> filteredCalls = grouper.getFilteredCalls(calls, maxMisses);
		for (MethodCallCount call : filteredCalls) {
			boolean hit = matcher.matches(call);
			if (hit && !reachedHit) {
				report.append("\nHits:\n");
				reachedHit = true;
			}
			if (!hit && !reachedMiss) {
				report.append("\nNear matches:\n");
				reachedMiss = true;
			}

			report.append(call.toString() + count(call) + "\n" + getStacktrace(call));
		}
		if (filteredCalls.size() < calls.size()) {
			report.append("... there were " + (calls.size() - filteredCalls.size()) + " more interactions with the mock.\n");
		}
		return report.toString();
	}

	private String count(MethodCallCount call) {
		if (call.getCount() <= 1) {
			return "";
		}
		return "\t\t[invoked " + call.getCount() + " times]";
	}

	private String getStacktrace(MethodCall call) {
		String traceString = call.getStackTraceString(1);
		if (traceString == null) {
			return "";
		}
		return traceString;
	}
}
