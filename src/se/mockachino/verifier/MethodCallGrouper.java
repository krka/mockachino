package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.matchers.MethodMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodCallGrouper {
	private final MethodMatcher matcher;
	private final Iterable<Invocation> calls;

	public MethodCallGrouper(MethodMatcher matcher, Iterable<Invocation> calls) {
		this.matcher = matcher;
		this.calls = calls;
	}

	public List<InvocationCount> getFilteredCalls(List<InvocationCount> calls, int maxMisses) {
		List<InvocationCount> res = new ArrayList<InvocationCount>();
		int numMisses = 0;

		for (InvocationCount call : calls) {
			boolean hit = matcher.matches(call.getMethodCall());
			if (!hit) {
				numMisses += 1;
			}
			if (hit || numMisses <= maxMisses) {
				res.add(call);
			}
		}
		return res;
	}

	public List<InvocationCount> getGroupedCalls() {
		List<Invocation> list = sortCalls();

		return getGroupedCalls(list);

	}

	public List<InvocationCount> getGroupedCalls(List<Invocation> list) {
		List<InvocationCount> res = new ArrayList<InvocationCount>();

		Invocation prev = null;
		InvocationCount current = null;
		for (Invocation call : list) {
			if (call.equals(prev)) {
				current.increment(1);
			} else {
				if (current != null) {
					res.add(current);
				}
				current = new InvocationCount(call);
				prev = call;
			}
		}
		if (current != null) {
			res.add(current);
		}
		return res;
	}

	public List<Invocation> sortCalls() {
		ArrayList<Invocation> list = new ArrayList<Invocation>();
		for (Invocation call : calls) {
			list.add(call);
		}
		Collections.sort(list, new InvocationComparator(matcher.getMethod(), matcher.getArgumentMatchers()));
		return list;
	}
}
