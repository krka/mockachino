package se.mockachino.mock;

import se.mockachino.*;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.stub.Stubber;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.SequentialAnswers;
import se.mockachino.util.MockachinoMethod;

public class WhenStubber<T> {
	static final ThreadLocal<Invocation<?>> lastInvocation = new ThreadLocal<Invocation<?>>();

	private final MockData<Object> data;
	private final Object mock;
	private final MockachinoMethod<T> method;
	private final MethodMatcher<T> matcher;

    private Stubber<T> stub;

    public WhenStubber() {
		Invocation<?> invocation = lastInvocation.get();
		if (invocation == null) {
			throw new UsageError("Illegal when(X).thenReturn(..) expression. X needs to be on the form mock.method(...)");
		}
		lastInvocation.set(null);

		mock = invocation.getObject();

		// Ensure that the mock is actually a mock
		data = Mockachino.getData(mock);

        MethodCall<T> methodCall = (MethodCall<T>) invocation.getMethodCall();
        method = methodCall.getMethod();
		matcher = new MethodMatcherImpl(method, methodCall.getArguments());

		// This invocation is a stubbing, not a call
		data.deleteLastInvocation();
	}

    private void addStub(VerifyableCallHandler<T> answer) {
        if (stub == null) {
            stub = new Stubber<T>(new SequentialAnswers<T>(answer));
            stub.onMethod(mock, method, matcher);
        } else {
            stub.extend(answer);
        }
    }

	public WhenStubber<T> thenReturn(T value) {
        addStub(new ReturnAnswer<T>(value));
        return this;
	}

	public WhenStubber<T> thenReturn(T value, T... values) {
        addStub(new ReturnAnswer<T>(value));
        if (values != null) {
            for (T t : values) {
                addStub(new ReturnAnswer<T>(t));
            }
        }
        return this;
	}

	public WhenStubber<T> thenAnswer(CallHandler<T> answer) {
        addStub(new VerifyableCallHandlerWrapper<T>(answer));
        return this;
	}

	public WhenStubber<T> thenThrow(Throwable t) {
        addStub(new ThrowAnswer<T>(t));
        return this;
	}

}
