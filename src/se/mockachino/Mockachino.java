package se.mockachino;

import se.mockachino.listener.ListenerAdder;
import se.mockachino.listener.MethodCallListener;
import se.mockachino.order.InOrder;
import se.mockachino.MockContext;
import se.mockachino.stub.StubAnswer;
import se.mockachino.stub.StubReturn;
import se.mockachino.stub.StubThrow;
import se.mockachino.verifier.VerifyRangeStart;

import java.lang.reflect.InvocationHandler;
import java.util.List;

public class Mockachino {
	private static final MockContext DEFAULT_CONTEXT = new MockContext();

	public static <T> T mock(Class<T> clazz) {
		return DEFAULT_CONTEXT.mock(clazz);
	}

	public static <T> T mock(Class<T> clazz, InvocationHandler handler) {
		return DEFAULT_CONTEXT.mock(clazz, handler);
	}

	public static <T> T spy(Class<T> clazz, T impl) {
		return DEFAULT_CONTEXT.spy(clazz, impl);
	}

	public static InOrder verifyOrder() {
		return DEFAULT_CONTEXT.verifyOrder();
	}

	public static VerifyRangeStart verifyNever() {
		return DEFAULT_CONTEXT.verifyNever();
	}

	public static VerifyRangeStart verifyOnce() {
		return DEFAULT_CONTEXT.verifyOnce();
	}

	public static VerifyRangeStart verifyAtLeast(int min) {
		return DEFAULT_CONTEXT.verifyAtLeast(min);
	}

	public static VerifyRangeStart verifyAtMost(int max) {
		return DEFAULT_CONTEXT.verifyAtMost(max);
	}

	public static VerifyRangeStart verifyRange(int min, int max) {
		return DEFAULT_CONTEXT.verifyRange(min, max);
	}

	public static VerifyRangeStart verifyExactly(int count) {
		return DEFAULT_CONTEXT.verifyExactly(count);
	}

	public static StubThrow stubThrow(Throwable e) {
		return DEFAULT_CONTEXT.stubThrow(e);
	}

	public static StubReturn stubReturn(Object returnValue) {
		return DEFAULT_CONTEXT.stubReturn(returnValue);
	}

	public static StubAnswer stubAnswer(Answer answer) {
		return DEFAULT_CONTEXT.stubAnswer(answer);
	}

	public static ListenerAdder listenWith(MethodCallListener listener) {
		return DEFAULT_CONTEXT.listenWith(listener);
	}

	public static List<MethodCall> getCalls(Object mock) {
		return DEFAULT_CONTEXT.getCalls(mock);
	}

}
