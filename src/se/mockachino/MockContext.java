package se.mockachino;

import se.mockachino.invocationhandler.*;
import se.mockachino.listener.AddListener;
import se.mockachino.listener.MethodCallListener;
import se.mockachino.mock.Mock;
import se.mockachino.order.InOrder;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.stub.Stubber;
import se.mockachino.stub.Thrower;

import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class MockContext {
	private static final InvocationHandler DEFAULT_INVOCATION_HANDLER = new DefaultInvocationHandler();

	private final Map<Object, MockData> mockData = Collections.synchronizedMap(new WeakHashMap<Object, MockData>());
	private final AtomicInteger nextSequenceNumber = new AtomicInteger();

	public <T> T mock(Class<T> clazz) {
		return mock(clazz, DEFAULT_INVOCATION_HANDLER);
	}

	public <T> T mock(Class<T> clazz, InvocationHandler handler) {
		return spy(clazz, ProxyUtil.newProxy(clazz, handler), "Mock");
	}

	public <T> T spy(Class<T> clazz, T impl) {
		return spy(clazz, impl, "Spy");
	}

	private <T> T spy(Class<T> clazz, T impl, String kind) {
		assertInterface(clazz);
		MatcherThreadHandler.assertEmpty();
		T mock = ProxyUtil.newProxy(clazz, new Mock(this, impl, kind));
		mockData.put(mock, new MockData(clazz));
		return mock;
	}

	public InOrder verifyOrder() {
		MatcherThreadHandler.assertEmpty();
		return new InOrder(this);
	}

	public <T> T verify(T mock) {
		return verifyAtLeast(mock, 1);
	}


	public <T> T verifyExactly(T mock, int count) {
		return verifyRange(mock, count, count);
	}

	public <T> T verifyAtMost(T mock, int max) {
		return verifyRange(mock, 0, max);
	}

	public <T> T verifyAtLeast(T mock, int min) {
		return verifyRange(mock, min, Integer.MAX_VALUE);
	}

	public <T> T verifyNever(T mock) {
		return verifyAtMost(mock, 0);
	}

	public <T> T verifyRange(T mock, int min, int max) {
		MatcherThreadHandler.assertEmpty();
		MockData<T> data = getData(mock);
		return data.getVerifier(min, max);
	}

	private <T> T createProxy(T mock, InvocationHandler handler) {
		MatcherThreadHandler.assertEmpty();
		MockData data = getData(mock);
		Class<T> iface = data.getInterface();
		return ProxyUtil.newProxy(iface, handler);
	}

	public <T> T stubThrow(T mock, Throwable e) {
		MockData data = getData(mock);
		return createProxy(mock, new Thrower(e, data));
	}

	public <T> T stubReturn(T mock, Object returnValue) {
		MockData data = getData(mock);
		return createProxy(mock, new Stubber(returnValue, data));
	}

	public <T> T addListener(T mock, MethodCallListener listener) {
		MockData data = getData(mock);
		return createProxy(mock, new AddListener(data, mock, listener));
	}

	private <T> void assertInterface(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Only interfaces can be mocked");
		}
	}

	public <T> MockData<T> getData(T mock) {
		MockData data = mockData.get(mock);
		if (data == null) {
			throw new IllegalArgumentException("Argument " + mock + " is not a mocked object.");
		}
		return data;
	}

	public List<MethodCall> getCalls(Object mock) {
		return getData(mock).getCalls();
	}

	public int incrementSequence() {
		return nextSequenceNumber.incrementAndGet();
	}
}