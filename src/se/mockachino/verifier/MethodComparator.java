package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.Primitives;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MethodComparator implements Comparator<MethodCall> {
	private final MockachinoMethod method;
	private final ArrayList<Matcher> argumentMatchers;

	public MethodComparator(MethodMatcher matcher) {
		method = matcher.getMethod();
		argumentMatchers = matcher.getArgumentMatchers();
	}

	@Override
	public int compare(MethodCall o1, MethodCall o2) {
		// First compare on names
		{
			int value = comp(nameMatches(o1), nameMatches(o2));
			if (value != 0) {
				return value;
			}
		}

		// Compare on method signature
		{
			int value = comp(methodMatches(o1), methodMatches(o2));
			if (value != 0) {
				return value;
			}
		}



		// Compare on actual arguments and types
		{
			Class[] parameters = method.getParameters();

			int i = 0;
			for (Matcher matcher : argumentMatchers) {
				int value = comp(matchNull(o1, i), matchNull(o2, i));
				if (value != 0) {
					return value;
				}

				value = comp(matchArg(o1, i, matcher), matchArg(o2, i, matcher));
				if (value != 0) {
					return value;
				}

				value = comp(matchType(o1, i, parameters), matchType(o1, i, parameters));
				if (value != 0) {
					return value;
				}

				i++;
			}
		}

		// Compare number of arguments
		int value = comp(compareLength(o1), compareLength(o2));
		if (value != 0) {
			return value;
		}

		// Compare on argument hashcodes in order to put equal
		// argument lists next to each other

		return specialHash(o1) - specialHash(o2);
	}

	private int specialHash(MethodCall o1) {
		StackTraceElement[] stackTraceElements = o1.getStackTrace();
		int stacktracehash;
		if (stackTraceElements.length >= 1) {
			stacktracehash = stackTraceElements[0].hashCode();
		} else {
			stacktracehash = 0;
		}
		return Arrays.hashCode(o1.getArguments()) + stacktracehash;
	}

	private boolean matchNull(MethodCall methodCall, int index) {
		Object[] args = methodCall.getArguments();
		if (index >= args.length) {
			return false;
		}
		return args[index] != null;
	}

	private boolean compareLength(MethodCall o1) {
		return o1.getArguments().length == argumentMatchers.size();
	}

	private int comp(boolean first, boolean second) {
		if (first && !second) {
			return -1;
		} else if (second && !first) {
			return 1;
		}
		return 0;
	}

	private boolean matchType(MethodCall methodCall, int index, Class[] parameters) {
		Object[] args = methodCall.getArguments();
		if (index >= args.length) {
			return false;
		}
		Object obj = args[index];
		if (obj == null) {
			return false;
		}
		Class<?> realClass = Primitives.getRealClass(parameters[index]);
		return realClass.isInstance(obj);
	}

	private boolean matchArg(MethodCall methodCall, int index, Matcher matcher) {
		Object[] args = methodCall.getArguments();
		if (index >= args.length) {
			return false;
		}
		return matcher.matches(args[index]);
	}

	private boolean methodMatches(MethodCall o1) {
		return o1.getMethod().equals(method);
	}

	private boolean nameMatches(MethodCall o1) {
		return o1.getMethod().getName().equals(method.getName());
	}
}
