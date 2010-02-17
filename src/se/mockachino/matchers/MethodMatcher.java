package se.mockachino.matchers;

import se.mockachino.MethodCall;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public interface MethodMatcher {
	boolean matches(MethodCall methodCall);
	List<Matcher> getArgumentMatchers();
	MockachinoMethod getMethod();
}
