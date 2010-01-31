package se.mockachino;

import se.mockachino.order.InOrder;
import se.mockachino.MockContext;
import se.mockachino.matchers.MatcherThreadHandler;

public class Mockachino {
	private static final MockContext DEFAULT_CONTEXT = new MockContext();

	public static <T> T mock(Class<T> clazz) {
		return DEFAULT_CONTEXT.mock(clazz);
	}

	public static <T> T spy(Class<T> clazz, T impl) {
		return DEFAULT_CONTEXT.spy(clazz, impl);
	}

	public static InOrder verifyOrder() {
		return DEFAULT_CONTEXT.verifyOrder();
	}

	public static <T> T verify(T mock) {
		return DEFAULT_CONTEXT.verify(mock);
	}

	public static <T> T verifyNever(T mock) {
		return DEFAULT_CONTEXT.verifyNever(mock);
	}

	public static <T> T verifyAtLeast(T mock, int min) {
		return DEFAULT_CONTEXT.verifyAtLeast(mock, min);
	}

	public static <T> T verifyAtMost(T mock, int max) {
		return DEFAULT_CONTEXT.verifyAtMost(mock, max);
	}

	public static <T> T verifyExactly(T mock, int count) {
		return DEFAULT_CONTEXT.verifyExactly(mock, count);
	}

	public static <T> T verifyRange(T mock, int min, int max) {
		return DEFAULT_CONTEXT.verifyRange(mock, min, max);
	}

	public static <T> T stubThrow(T mock, Throwable e) {
		return DEFAULT_CONTEXT.stubThrow(mock, e);
	}

	public static <T> T stubReturn(T mock, Object returnValue) {
		return DEFAULT_CONTEXT.stubReturn(mock, returnValue);
	}
}
