package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.MockData;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.MockPoint;

import java.util.ArrayList;
import java.util.List;

public class VerifyHandler<T> extends MatchingHandler {
	private final MockData<T> mockData;
	private final int minCalls;
	private final int maxCalls;
	private final MockPoint start;
	private final MockPoint end;

	public VerifyHandler(T mock, MockData<T> mockData,
						 int minCalls, int maxCalls,
						 MockPoint start, MockPoint end) {
		super("VerifyHandler", mock.toString());
		this.mockData = mockData;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
		this.start = start;
		this.end = end;
	}

	@Override
	public void match(Object o, MethodMatcher matcher) {
		int counter = 0;
		List<MethodCall> calls = filter(mockData.getCalls(), start, end);
		for (MethodCall call : calls) {
			if (matcher.matches(call)) {
				counter++;
			}
		}
		String errorMessage = new Reporter(counter, minCalls, maxCalls).getErrorLine();
		if (errorMessage != null) {
			boolean wantsMisses = minCalls > 0;
			error(calls, errorMessage, matcher, wantsMisses ? 3 : 0);

		}
	}

	private List<MethodCall> filter(List<MethodCall> calls, MockPoint start, MockPoint end) {
		int startNumber = start.getCallNumber();
		int endNumber = end.getCallNumber();
		List<MethodCall> res = new ArrayList<MethodCall>();
		for (MethodCall call : calls) {
			if (call.getCallNumber() < startNumber) {
				continue;
			}
			if (call.getCallNumber() > endNumber) {
				break;
			}
			res.add(call);
		}
		return res;
	}

	private void error(List<MethodCall> calls, String msg, MethodMatcher matcher, int maxMisses) {
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
			report.append("... skipping " + (calls.size() - filteredCalls.size()) + " calls\n");
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
