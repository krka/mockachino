package se.mockachino.stub;

import se.mockachino.*;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.SequentialAnswers;
import se.mockachino.util.MockachinoMethod;

public class Stubber<T> {
	private final SequentialAnswers<T> answer;
    private MockachinoMethod method;

    public Stubber(SequentialAnswers<T> answer) {
        this.answer = answer;
    }

    public <U> U on(U mock) {
		MockData data = Mockachino.getData(mock);
		return ProxyUtil.createProxy(mock, new StubHandler(answer, data));
	}


	public void onMethod(Object mock, MockachinoMethod method, MethodMatcher matcher) {
        this.method = method;
        answer.verify(method);
        MockData data = Mockachino.getData(mock);
        MethodStubs<T> methodStubs = data.getStubs(method);
        methodStubs.add(new MethodStub<T>(answer, matcher));
    }

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		onMethod(mock, method, MethodMatcherImpl.matchAll(method));
	}

	public void onAnyMethod(Object mock) {
		MockData<Object> data = Mockachino.getData(mock);
		for (MockachinoMethod method : data.getMethods()) {
			onMethodWithAnyArgument(mock, method);
		}
	}

    public void extend(VerifyableCallHandler<T> answer) {
        this.answer.add(answer);
        if (method != null) {
            this.answer.verify(method);
        }
    }

    public Stubber<T> thenReturn(T returnValue) {
        extend(new ReturnAnswer<T>(returnValue));
        return this;
    }

    public Stubber<T> thenReturn(T returnValue, T... returnValues) {
        extend(new ReturnAnswer<T>(returnValue));
        if (returnValues != null) {
            for (T value : returnValues) {
                extend(new ReturnAnswer<T>(value));
            }
        }
        return this;
    }

    public Stubber<T> thenAnswer(CallHandler<T> answer) {
        extend(new VerifyableCallHandlerWrapper<T>(answer));
        return this;
    }

    public Stubber<T> thenThrow(Throwable throwable) {
        extend(new ThrowAnswer<T>(throwable));
        return this;
    }
}
