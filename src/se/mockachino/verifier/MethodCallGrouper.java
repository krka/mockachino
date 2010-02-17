package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodCallGrouper {
	private final MethodMatcher matcher;
	private final Iterable<MethodCall> calls;

	public MethodCallGrouper(MethodMatcher matcher, Iterable<MethodCall> calls) {
		this.matcher = matcher;
		this.calls = calls;
	}

	public List<MethodCallCount> getFilteredCalls(List<MethodCallCount> calls, int maxMisses) {
		List<MethodCallCount> res = new ArrayList<MethodCallCount>();
		int numMisses = 0;

		for (MethodCallCount call : calls) {
			boolean hit = matcher.matches(call);
			if (!hit) {
				numMisses += 1;
			}
			if (hit || numMisses <= maxMisses) {
				res.add(call);
			}
		}
		return res;
	}

	public List<MethodCallCount> getGroupedCalls() {
		List<MethodCall> list = sortCalls();

		return getGroupedCalls(list);

	}

	public List<MethodCallCount> getGroupedCalls(List<MethodCall> list) {
		List<MethodCallCount> res = new ArrayList<MethodCallCount>();

		MethodCall prev = null;
		MethodCallCount current = null;
		for (MethodCall call : list) {
			if (call.equals(prev)) {
				current.increment(1);
			} else {
				if (current != null) {
					res.add(current);
				}
				current = new MethodCallCount(call);
				prev = call;
			}
		}
		if (current != null) {
			res.add(current);
		}
		return res;
	}

	public List<MethodCall> sortCalls() {
		List<MethodCall> list = new ArrayList<MethodCall>();
		for (MethodCall call : calls) {
			list.add(call);
		}
		Collections.sort(list, new MethodComparator(matcher.getMethod(), matcher.getArgumentMatchers()));
		return list;
	}
}
