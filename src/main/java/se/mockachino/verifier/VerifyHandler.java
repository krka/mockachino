package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.InOrderVerifyHandler;
import se.mockachino.util.MockachinoMethod;

import java.util.Iterator;
import java.util.List;

public class VerifyHandler<T> extends MatchingHandler<T> {
	private static final BadUsageHandler BAD_USAGE_HANDLER = InOrderVerifyHandler.BAD_USAGE_HANDLER;

	private final Iterable<Invocation<T>> calls;
	private final int minCalls;
	private final int maxCalls;
	private final long timeout;

	public VerifyHandler(Object mock, Iterable<Invocation<T>> calls,
						 int minCalls, int maxCalls, long timeout) {
		super("VerifyHandler", Mockachino.getData(mock).getName(), BAD_USAGE_HANDLER, Mockachino.getData(mock).getTypeLiteral());
		this.calls = calls;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
		this.timeout = timeout;
	}

	@Override
	public void match(Object o, MockachinoMethod<T> method, MethodMatcher<T> matcher) {
		long startTime = System.currentTimeMillis();
		int counter = 0;
		Iterator<Invocation<T>> invocationIterator = calls.iterator();
		counter = consumeIterator(matcher, counter, invocationIterator);

		while (shouldWait(startTime)) {

			// No point in waiting if we have already exceeded the max count
			if (counter > maxCalls) {
				break;
			}

			// We must wait as long as we have too few calls.
			if (maxCalls == Integer.MAX_VALUE) {
				if (counter >= minCalls) {
					break;
				}
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
			counter = consumeIterator(matcher, counter, invocationIterator);
		}
		String errorMessage = new Reporter(counter, minCalls, maxCalls).getErrorLine();
		if (errorMessage != null) {
			boolean wantsMisses = minCalls > 0;
			error(errorMessage, matcher, wantsMisses ? 3 : 0);
		}
	}

	private boolean shouldWait(long startTime) {
		return System.currentTimeMillis() - startTime < timeout;
	}

	private int consumeIterator(MethodMatcher<T> matcher, int counter, Iterator<Invocation<T>> invocationIterator) {
		while (invocationIterator.hasNext()) {
			Invocation<T> invocation = invocationIterator.next();
			if (matcher.matches(invocation.getMethodCall())) {
				counter++;
			}
		}
		return counter;
	}

	private void error(String msg, MethodMatcher<T> matcher, int maxMisses) {
		MethodCallGrouper grouper = new MethodCallGrouper(matcher, calls);
		grouper.getGroupedCalls();
		String matchingMethods = getBestMatches(matcher, maxMisses, grouper);
		throw new VerificationError(msg + matchingMethods + "\n... for the verification at:");
	}

	private String getBestMatches(MethodMatcher matcher, int maxMisses, MethodCallGrouper grouper) {
		StringBuilder report = new StringBuilder();
		report.append("\nMethod pattern:\n" + matcher.toString() + "\n");

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
