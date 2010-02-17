package se.mockachino.stub;

import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class ReturnValueStub implements se.mockachino.expectations.MethodStub {
	private final Object returnValue;
	private final MethodMatcher matcher;

	public ReturnValueStub(Object returnValue, MethodMatcher matcher) {
		this.returnValue = returnValue;
		this.matcher = matcher;
	}

	@Override
	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	@Override
	public List<Matcher> getArgumentMatchers() {
		return matcher.getArgumentMatchers();
	}

	@Override
	public MockachinoMethod getMethod() {
		return matcher.getMethod();
	}

	@Override
	public Object invoke(Object mock, MethodCall call) throws Throwable {
		return returnValue;
	}
}
