package se.mockachino.stub;

import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class ExceptionStub implements se.mockachino.expectations.MethodStub {
	private final Throwable e;
	private final MethodMatcher matcher;

	public ExceptionStub(Throwable e, MethodMatcher matcher) {
		this.e = e;
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
		throw e;
	}
}