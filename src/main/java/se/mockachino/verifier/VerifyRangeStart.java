package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.order.MockPoint;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.util.MockachinoMethod;

public class VerifyRangeStart {
	private final int min;
	private final int max;
	private final MockPoint start;
	private final MockPoint end;

	private long timeout;

	public VerifyRangeStart(int min, int max) {
		this(min, max, Mockachino.BIG_BANG, Mockachino.BIG_CRUNCH);
	}

	public VerifyRangeStart(int min, int max, MockPoint start, MockPoint end) {
		this.min = min;
		this.max = max;
		this.start = start;
		this.end = end;
	}


	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		Iterable<Invocation> calls = data.getCalls(start, end);
		VerifyHandler verifyHandler = new VerifyHandler(mock, calls, min, max, timeout);
		return ProxyUtil.createProxy(mock, verifyHandler);
	}

	public void onMethod(Object mock, MethodMatcher matcher) {
		MockData data = Mockachino.getData(mock);

		final VerifyHandler verifyHandler = new VerifyHandler(mock, data.getCalls(start, end), min, max, timeout);
		verifyHandler.match(null, null, matcher);
	}

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		onMethod(mock, MethodMatcherImpl.matchAll(method));
	}

	public void onAnyMethod(Object mock) {
		MockData data = Mockachino.getData(mock);

		final VerifyHandler verifyHandler = new VerifyHandler(mock, data.getCalls(start, end), min, max, timeout);
		verifyHandler.match(null, null, MatchAny.INSTANCE);
	}

	public VerifyRangeStart withTimeout(long milliseconds) {
		this.timeout = milliseconds;
		return this;
	}

}
