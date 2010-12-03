package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.InOrderVerifyHandler;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class VerifyHandler<T> extends MatchingHandler {
    private static final BadUsageHandler BAD_USAGE_HANDLER = InOrderVerifyHandler.BAD_USAGE_HANDLER;
    
    private final Iterable<Invocation> calls;
	private final int minCalls;
	private final int maxCalls;

	public VerifyHandler(T mock, Iterable<Invocation> calls,
						 int minCalls, int maxCalls) {
		super("VerifyHandler", Mockachino.getData(mock).getName(), BAD_USAGE_HANDLER);
		this.calls = calls;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		int counter = 0;
		for (Invocation call : calls) {
			if (matcher.matches(call.getMethodCall())) {
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
		throw new VerificationError(msg + matchingMethods + "\n... for the verification at:");
	}

	private String getBestMatches(MethodMatcher matcher, int maxMisses, MethodCallGrouper grouper) {
		StringBuilder report = new StringBuilder();
		report.append("\nMethod pattern:\n"  + matcher.toString() + "\n");

		boolean reachedHit = false;
		boolean reachedMiss = false;
		List<InvocationCount> calls = grouper.getGroupedCalls();
		List<InvocationCount> filteredCalls = grouper.getFilteredCalls(calls, maxMisses);
		for (InvocationCount call : filteredCalls) {
			boolean hit = matcher.matches(call.getMethodCall());
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
		/*
		if (filteredCalls.size() < calls.size()) {
			report.append("... there were " + (calls.size() - filteredCalls.size()) + " more interactions with the mock, but they are not shown here.\n");

		}
		*/
		return report.toString();
	}

	private String count(InvocationCount call) {
		if (call.getCount() <= 1) {
			return "";
		}
		return "\t\t[invoked " + call.getCount() + " times]";
	}

	private String getStacktrace(Invocation call) {
		String traceString = call.getStackTraceString(1);
		if (traceString == null) {
			return "";
		}
		return traceString;
	}
}
