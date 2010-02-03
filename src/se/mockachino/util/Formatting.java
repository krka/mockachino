package se.mockachino.util;

import se.mockachino.matchers.matcher.Matcher;

public class Formatting {
	public static String list(Object[] args) {
		return join(", ", args);
	}

	public static String list(Iterable<Matcher> iterable) {
		return join(", ", iterable);
	}

	public static String argument(Object arg) {
		if (arg == null) {
			return "null";
		}
		if (arg instanceof String) {
			return "\"" + arg + "\"";
		}
		return arg.toString();
	}

	public static String join(String sep, Iterable iterable) {
		if (iterable == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object object : iterable) {
			if (first) {
				first = false;
			} else {
				builder.append(sep);
			}
			builder.append(argument(object));
		}
		return builder.toString();
	}

	public static String join(String sep, Object... objects) {
		if (objects == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object object : objects) {
			if (first) {
				first = false;
			} else {
				builder.append(sep);
			}
			builder.append(argument(object));
		}
		return builder.toString();
	}
	public static String toString(StackTraceElement[] stacktrace) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement element : stacktrace) {
			builder.append("\tat ").append(element.getClassName()).append(".").append(element.getMethodName());
			builder.append("(").append(element.getFileName()).append(":").append(element.getLineNumber()).append(")");
			builder.append("\n");
		}
		return builder.toString();
	}

}
