package se.mockachino;

import se.mockachino.expectations.MethodStubs;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.observer.MethodObserver;
import se.mockachino.order.MockPoint;
import se.mockachino.order.MockPointIterable;
import se.mockachino.stub.AnswerStub;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.util.SafeIteratorList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockData<T> {
	private static final CallHandler DEFAULT_EQUALS = new CallHandler() {
		@Override
		public Object invoke(Object obj, MethodCall call) throws Throwable {
			return obj == call.getArguments()[0];
		}
	};
	private static final MethodMatcher EQUALS_METHOD_MATCHER = MethodMatcherImpl.matchAll(MockachinoMethod.EQUALS);
	private static final AnswerStub DEFAULT_EQUALS_STUB = new AnswerStub(DEFAULT_EQUALS, EQUALS_METHOD_MATCHER);

	private static final CallHandler DEFAULT_HASHCODE = new CallHandler() {
		@Override
		public Object invoke(Object obj, MethodCall call) throws Throwable {
			return System.identityHashCode(obj);
		}
	};
	private static final MethodMatcher HASHCODE_METHOD_MATCHER = MethodMatcherImpl.matchAll(MockachinoMethod.HASHCODE);
	private static final AnswerStub DEFAULT_HASHCODE_STUB = new AnswerStub(DEFAULT_HASHCODE, HASHCODE_METHOD_MATCHER);

	public final static MethodCall NULL_METHOD = new MethodCall(MockachinoMethod.NULL, new Object[]{}, 0, new StackTraceElement[]{});
	private final Class<T> iface;
	private final Set<Class<?>> extraInterfaces;
	private final List<MethodCall> calls;
	private final List<MethodCall> readOnlyCalls;
	private final Map<MockachinoMethod, List<MethodObserver>> observers;
	private final Map<MockachinoMethod, MethodStubs> stubs;

	public MockData(Class<T> iface, Set<Class<?>> extraInterfaces) {
		this.iface = iface;
		this.extraInterfaces = extraInterfaces;
		calls = new SafeIteratorList<MethodCall>(new ArrayList<MethodCall>(), NULL_METHOD);
		readOnlyCalls = Collections.unmodifiableList(calls);
		observers = new HashMap<MockachinoMethod,List<MethodObserver>>();

		stubs = new HashMap<MockachinoMethod, MethodStubs>();
		for (Method reflectMethod : iface.getMethods()) {
			addExpectation(reflectMethod);
		}
		for (Method reflectMethod : Object.class.getMethods()) {
			addExpectation(reflectMethod);
		}
		for (Class<?> extraInterface : extraInterfaces) {
			for (Method reflectMethod : extraInterface.getMethods()) {
				addExpectation(reflectMethod);
			}
		}
		setupEqualsAndHashcode();
	}

	private void addExpectation(Method reflectMethod) {
		MockachinoMethod method = new MockachinoMethod(reflectMethod);
		stubs.put(method, new MethodStubs());
		observers.put(method,
				new SafeIteratorList<MethodObserver>(
						new ArrayList<MethodObserver>(), null));
	}

	public Iterable<MethodCall> getCalls() {
		return readOnlyCalls;
	}

	public Iterable<MethodCall> getCalls(MockPoint start, MockPoint end) {
		return new MockPointIterable(readOnlyCalls, start, end);
	}

	public MethodStubs getExpectations(MockachinoMethod method) {
		return stubs.get(method);
	}

	public List<MethodObserver> getObservers(MockachinoMethod method) {
		return observers.get(method);
	}

	public Class<T> getInterface() {
		return iface;
	}

	public synchronized MethodCall addCall(MockContext mockContext, MockachinoMethod method, Object[] objects, StackTraceElement[] stackTrace) {
		int callNumber = mockContext.incrementSequence();
		MethodCall call = new MethodCall(method, objects, callNumber, stackTrace);
		calls.add(call);
		return call;
	}

	public synchronized void resetCalls() {
		calls.clear();
	}

	public synchronized void resetStubs() {
		for (MethodStubs methodStubs : stubs.values()) {
			methodStubs.clear();
		}
		setupEqualsAndHashcode();
	}

	private void setupEqualsAndHashcode() {
		MethodStubs equalsStubs = stubs.get(MockachinoMethod.EQUALS);
		equalsStubs.add(DEFAULT_EQUALS_STUB);

		MethodStubs hashcodeStubs = stubs.get(MockachinoMethod.HASHCODE);
		hashcodeStubs.add(DEFAULT_HASHCODE_STUB);
	}


	public synchronized void resetObservers() {
		for (List<MethodObserver> methodObservers : observers.values()) {
			methodObservers.clear();
		}
	}

	public Set<Class<?>> getExtraInterfaces() {
		return extraInterfaces;		
	}
}
