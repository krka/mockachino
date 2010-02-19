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
	private static final MethodStub DEFAULT_EQUALS_STUB = new MethodStub(DEFAULT_EQUALS, EQUALS_METHOD_MATCHER);

	private static final CallHandler DEFAULT_HASHCODE = new CallHandler() {
		@Override
		public Object invoke(Object obj, MethodCall call) throws Throwable {
			return System.identityHashCode(obj);
		}
	};
	private static final MethodMatcher HASHCODE_METHOD_MATCHER = MethodMatcherImpl.matchAll(MockachinoMethod.HASHCODE);
	private static final MethodStub DEFAULT_HASHCODE_STUB = new MethodStub(DEFAULT_HASHCODE, HASHCODE_METHOD_MATCHER);

	private final MockContext context;
	private final Class<T> iface;
	private final Set<Class<?>> extraInterfaces;
	private final List<MethodCall> calls;
	private final List<MethodCall> readOnlyCalls;
	private final Map<MockachinoMethod, List<MethodObserver>> observers;
	private final Map<MockachinoMethod, MethodStubs> stubs;

	public MockData(MockContext context, Class<T> iface, Set<Class<?>> extraInterfaces) {
		this.context = context;
		this.iface = iface;
		this.extraInterfaces = extraInterfaces;
		calls = new SafeIteratorList<MethodCall>(new ArrayList<MethodCall>(), MethodCall.NULL);
		readOnlyCalls = Collections.unmodifiableList(calls);
		observers = new HashMap<MockachinoMethod,List<MethodObserver>>();

		stubs = new HashMap<MockachinoMethod, MethodStubs>();
		for (Method reflectMethod : iface.getMethods()) {
			addContainer(reflectMethod);
		}
		for (Method reflectMethod : Object.class.getMethods()) {
			addContainer(reflectMethod);
		}
		for (Class<?> extraInterface : extraInterfaces) {
			for (Method reflectMethod : extraInterface.getMethods()) {
				addContainer(reflectMethod);
			}
		}
		setupEqualsAndHashcode();
	}

	private void addContainer(Method reflectMethod) {
		MockachinoMethod method = new MockachinoMethod(reflectMethod);
		stubs.put(method, new MethodStubs());
		observers.put(method,
				new SafeIteratorList<MethodObserver>(
						new ArrayList<MethodObserver>(), null));
	}

	/**
	 * Gets a list of all the method calls made for the mock object
	 * @return the list of calls
	 */
	public Iterable<MethodCall> getCalls() {
		return readOnlyCalls;
	}

	/**
	 * Gets a list of all the method calls made for the mock object
	 * between (inclusive) two points in time.
	 * @return the list of calls
	 */
	public Iterable<MethodCall> getCalls(MockPoint start, MockPoint end) {
		return new MockPointIterable(readOnlyCalls, start, end);
	}

	/**
	 * Get all stubs for the mock and the method
	 * @param method
	 * @return the stubs
	 */
	public MethodStubs getStubs(MockachinoMethod method) {
		return stubs.get(method);
	}

	/**
	 * Get all observers hooked to a specific method on the mock
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
	 * @param objects
	 * @param stackTrace
	 * @return the method call which was added
	 */
	public synchronized MethodCall addCall(MockachinoMethod method, Object[] objects, StackTraceElement[] stackTrace) {
		int callNumber = context.incrementSequence();
		MethodCall call = new MethodCall(method, objects, callNumber, stackTrace);
		calls.add(call);
		return call;
	}

	/**
	 * Clear the list of calls
	 */
	public synchronized void resetCalls() {
		calls.clear();
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

	/**
	 * Get the set of additionally implemented interfaces by the mock.
	 * This may be an empty set if no other interfaces are defined.
	 * @return
	 */
	public Set<Class<?>> getExtraInterfaces() {
		return extraInterfaces;		
	}
}
