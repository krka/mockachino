package se.mockachino;

import se.mockachino.order.InOrder;
import se.mockachino.invocationhandler.DefaultInvocationHandler;
import se.mockachino.invocationhandler.Stubber;
import se.mockachino.invocationhandler.Mock;
import se.mockachino.invocationhandler.Thrower;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.MockData;
import se.mockachino.MethodCall;

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
		return spy(clazz, ProxyUtil.newProxy(clazz, DEFAULT_INVOCATION_HANDLER), "Mock");
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
		MatcherThreadHandler.assertEmpty();
		MockData<T> data = getData(mock);
		return data.getVerifier();
	}

	public <T> T verify(T mock, int min, int max) {
		MatcherThreadHandler.assertEmpty();
		MockData<T> data = getData(mock);
		return data.getVerifier(min, max);
	}

	public <T> T stubThrow(T mock, Throwable e) {
		MatcherThreadHandler.assertEmpty();
		MockData data = getData(mock);
		Class<T> iface = data.getInterface();
		return ProxyUtil.newProxy(iface, new Thrower(e, data));
	}

	public <T> T stubReturn(T mock, Object returnValue) {
		MatcherThreadHandler.assertEmpty();
		MockData data = getData(mock);
		Class<T> iface = data.getInterface();
		return ProxyUtil.newProxy(iface, new Stubber(returnValue, data));
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