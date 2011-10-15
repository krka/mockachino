package se.mockachino;

import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.observer.MethodObserver;
import se.mockachino.order.MockPoint;
import se.mockachino.order.MockPointIterable;
import se.mockachino.stub.MethodStub;
import se.mockachino.stub.MethodStubs;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.util.SafeIteratorList;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class MockData<T> {
	private static final CallHandler DEFAULT_EQUALS = new CallHandler() {
		@Override
		public Object invoke(Object obj, MethodCall call) throws Throwable {
			return obj == call.getArguments()[0];
		}
	};
	private static final MethodMatcher EQUALS_METHOD_MATCHER = MethodMatcherImpl.matchAll(MockachinoMethod.EQUALS);
	private static final MethodStub DEFAULT_EQUALS_STUB = new MethodStub(DEFAULT_EQUALS, EQUALS_METHOD_MATCHER);

	private static final CallHandler DEFAULT_HASHCODE = new CallHandler() {
		@Override
		public Object invoke(Object obj, MethodCall call) throws Throwable {
			return System.identityHashCode(obj);
		}
	};
	private static final MethodMatcher HASHCODE_METHOD_MATCHER = MethodMatcherImpl.matchAll(MockachinoMethod.HASHCODE);
	private static final MethodStub DEFAULT_HASHCODE_STUB = new MethodStub(DEFAULT_HASHCODE, HASHCODE_METHOD_MATCHER);

	private final Class<T> iface;
	private final List<Invocation> invocations;
	private final List<Invocation> readOnlyCalls;
	private final Map<MockachinoMethod, List<MethodObserver>> observers;
	private final Map<MockachinoMethod, MethodStubs> stubs;
	private final Type type;
	private final Set<MockachinoMethod> methods;
	private final String name;

	public MockData(Class<T> iface, Type type, String name) {
		this.iface = iface;
		this.type = type;
		this.name = name;
		invocations = new SafeIteratorList<Invocation>(new ArrayList<Invocation>(), Invocation.NULL);
		readOnlyCalls = Collections.unmodifiableList(invocations);
		observers = new HashMap<MockachinoMethod, List<MethodObserver>>();

		stubs = new HashMap<MockachinoMethod, MethodStubs>();
		addMethods(iface);
		addMethods(Object.class);
		methods = Collections.unmodifiableSet(stubs.keySet());
		setupEqualsAndHashcode();
	}

	private void addMethods(Class<?> clazz) {
		if (clazz == null) {
			return;
		}
		for (Class<?> superClass : clazz.getInterfaces()) {
			addMethods(superClass);
		}
		addMethods(clazz.getSuperclass());
		for (Method method : clazz.getDeclaredMethods()) {
			try {
				method.setAccessible(true);
				addContainer(method);
			} catch (SecurityException e) {
				// Ignore this method
			}
		}
	}

	private void addContainer(Method reflectMethod) {
		MockachinoMethod method = new MockachinoMethod(reflectMethod);
		if (stubs.containsKey(method)) {
			return;
		}
		stubs.put(method, new MethodStubs());
		observers.put(method,
				new SafeIteratorList<MethodObserver>(
						new ArrayList<MethodObserver>(), null));
	}

	public String getName() {
		return name;
	}

	public Set<MockachinoMethod> getMethods() {
		return methods;
	}

	/**
	 * Gets a list of all the method invocations made for the mock object
	 *
	 * @return the list of invocations
	 */
	public Iterable<Invocation> getInvocations() {
		return readOnlyCalls;
	}

	/**
	 * Gets a list of all the method invocations made for the mock object
	 * between (inclusive) two points in time.
	 *
	 * @return the list of invocations
	 */
	public Iterable<Invocation> getCalls(MockPoint start, MockPoint end) {
		return new MockPointIterable(readOnlyCalls, start, end);
	}

	/**
	 * Get all stubs for the mock and the method
	 *
	 * @param method
	 * @return the stubs
	 */
	public MethodStubs getStubs(MockachinoMethod method) {
		return stubs.get(method);
	}

	/**
	 * Get all observers hooked to a specific method on the mock
	 *
	 * @param method
	 * @return all observers
	 */
	public List<MethodObserver> getObservers(MockachinoMethod method) {
		return observers.get(method);
	}

	/**
	 * Get the interface of the mock
	 */
	public Class<T> getInterface() {
		return iface;
	}

	/**
	 * Add a call on the mock.
	 * This is typically only needed to be called by Mockachino internally.
	 *
	 * @param method
	 * @param args
	 * @param stackTrace
	 * @return the method call which was added
	 */
	public synchronized Invocation addCall(Object obj, MockachinoMethod method, Object[] args, StackTraceElement[] stackTrace) {
		int callNumber = MockUtil.incrementSequence();
		MethodCall call = new MethodCall(method, args);
		Invocation invocation = new Invocation(obj, call, callNumber, stackTrace);
		if (!method.isToStringCall()) {
			invocations.add(invocation);
		}
		return invocation;
	}

	/**
	 * Clear the list of invocations
	 */
	public synchronized void resetCalls() {
		invocations.clear();
	}

	/**
	 * Remove all stubbing on the mock
	 */
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


	/**
	 * Remove all observers from the mock
	 */
	public synchronized void resetObservers() {
		for (List<MethodObserver> methodObservers : observers.values()) {
			methodObservers.clear();
		}
	}

	public synchronized void deleteLastInvocation() {
		int index = invocations.size() - 1;
		if (index >= 0) {
			invocations.remove(index);
		}
	}

	public Type getTypeLiteral() {
		return type;
	}
}
