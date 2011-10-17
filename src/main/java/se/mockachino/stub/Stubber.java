package se.mockachino.stub;

import se.mockachino.*;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.mock.WhenStubber;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.SequentialAnswers;
import se.mockachino.util.MockachinoMethod;

public class Stubber {
	private final SequentialAnswers answer;
    private MockachinoMethod method;

    public Stubber(SequentialAnswers answer) {
        this.answer = answer;
    }

    public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		return ProxyUtil.createProxy(mock, new StubHandler(answer, data));
	}


	public void onMethod(Object mock, MockachinoMethod method, MethodMatcher matcher) {
        this.method = method;
        answer.verify(method);
        MockData data = Mockachino.getData(mock);
        MethodStubs methodStubs = data.getStubs(method);
        methodStubs.add(new MethodStub(answer, matcher));
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

    public void extend(VerifyableCallHandler answer) {
        this.answer.add(answer);
        if (method != null) {
            this.answer.verify(method);
        }
    }

    public Stubber thenReturn(Object returnValue) {
        extend(new ReturnAnswer(returnValue));
        return this;
    }

    public Stubber thenReturn(Object returnValue, Object... returnValues) {
        extend(new ReturnAnswer(returnValue));
        if (returnValues != null) {
            for (Object value : returnValues) {
                extend(new ReturnAnswer(value));
            }
        }
        return this;
    }

    public Stubber thenAnswer(CallHandler answer) {
        extend(new VerifyableCallHandlerWrapper(answer));
        return this;
    }

    public Stubber thenThrow(Throwable throwable) {
        extend(new ThrowAnswer(throwable));
        return this;
    }
}
