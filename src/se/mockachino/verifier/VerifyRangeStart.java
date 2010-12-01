package se.mockachino.verifier;

import se.mockachino.*;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.order.MockPoint;
import se.mockachino.util.MockachinoMethod;

public class VerifyRangeStart {
	private final MockContext mockContext;
	private final int min;
	private final int max;
	private final MockPoint start;
	private final MockPoint end;

	public VerifyRangeStart(MockContext mockContext, int min, int max) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
		start = mockContext.BIG_BANG;
		end = mockContext.BIG_CRUNCH;
	}

	public VerifyRangeStart(MockContext mockContext, int min, int max, MockPoint start, MockPoint end) {
		this.mockContext = mockContext;
		this.min = min;
		this.max = max;
		this.start = start;
		this.end = end;
	}


	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		Iterable<Invocation> calls = data.getCalls(start, end);
		VerifyHandler verifyHandler = new VerifyHandler(mock, calls, min, max);
		return mockContext.createProxy(mock, verifyHandler);
	}

    public void onMethod(Object mock, MethodMatcher matcher) {
        MockData data = Mockachino.getData(mock);

        final VerifyHandler verifyHandler = new VerifyHandler(mock, data.getCalls(start, end), min, max);
        verifyHandler.match(null, null, matcher);
    }

    public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
        onMethod(mock, MethodMatcherImpl.matchAll(method));
    }

    public void onAnyMethod(Object mock) {
        MockData data = Mockachino.getData(mock);

        final VerifyHandler verifyHandler = new VerifyHandler(mock, data.getCalls(start, end), min, max);
        verifyHandler.match(null, null, MatchAny.INSTANCE);
    }

}
