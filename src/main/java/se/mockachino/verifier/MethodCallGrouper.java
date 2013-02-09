package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.matchers.MethodMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodCallGrouper<T> {
	private final MethodMatcher<T> matcher;
	private final Iterable<Invocation<T>> calls;

	public MethodCallGrouper(MethodMatcher<T> matcher, Iterable<Invocation<T>> calls) {
		this.matcher = matcher;
		this.calls = calls;
	}

	public List<InvocationCount<T>> getFilteredCalls(List<InvocationCount<T>> calls, int maxMisses) {
		List<InvocationCount<T>> res = new ArrayList<InvocationCount<T>>();
		int numMisses = 0;

		for (InvocationCount<T> call : calls) {
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

	public List<InvocationCount<T>> getGroupedCalls() {
		List<Invocation<T>> list = sortCalls();

		return getGroupedCalls(list);

	}

	public List<InvocationCount<T>> getGroupedCalls(List<Invocation<T>> list) {
		List<InvocationCount<T>> res = new ArrayList<InvocationCount<T>>();

		Invocation<T> prev = null;
		InvocationCount<T> current = null;
		for (Invocation<T> call : list) {
			if (call.equals(prev)) {
				current.increment(1);
			} else {
				if (current != null) {
					res.add(current);
				}
				current = new InvocationCount<T>(call);
				prev = call;
			}
		}
		if (current != null) {
			res.add(current);
		}
		return res;
	}

	public List<Invocation<T>> sortCalls() {
		ArrayList<Invocation<T>> list = new ArrayList<Invocation<T>>();
		for (Invocation<T> call : calls) {
			list.add(call);
		}
		Collections.sort(list, new InvocationComparator<T>(matcher.getMethod(), matcher.getArgumentMatchers()));
		return list;
	}
}
