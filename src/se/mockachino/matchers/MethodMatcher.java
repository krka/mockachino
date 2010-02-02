package se.mockachino.matchers;

import se.mockachino.MethodCall;
import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.Formatting;

import java.util.ArrayList;
import java.lang.reflect.Method;

public class MethodMatcher {
	private final Method method;
	private final ArrayList<Matcher> argumentMatchers;

	public MethodMatcher(Method method, Object[] arguments) {
		this.method = method;
		argumentMatchers = new ArrayList<Matcher>();
		if (arguments != null) {
			if (MatcherThreadHandler.isClean()) {
				for (Object argument : arguments) {
					argumentMatchers.add(new EqualityMatcher(argument));
				}
			} else {
				for (Object argument : arguments) {
					argumentMatchers.add(MatcherThreadHandler.getMatcher(argument));
				}
			}
		}
	}

	public boolean matches(MethodCall methodCall) {
		if (!methodCall.getMethod().equals(method)) {
			return false;
		}

		Object[] arguments = methodCall.getArguments();
		if (arguments == null) {
			return argumentMatchers.size() == 0;
		}
		int n = arguments.length;
		if (argumentMatchers.size() != n) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			if (!argumentMatchers.get(i).matches(arguments[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return method.getName() + "(" + Formatting.list(argumentMatchers) + ")";
	}

	public Method getMethod() {
		return method;
	}
}
