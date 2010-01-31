package se.mockachino.util;

import se.mockachino.matchers.Matcher;

public class Formatting {
	public static String list(Object[] args) {
		StringBuilder builder = new StringBuilder();
		if (args != null) {
			for (Object arg : args) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(argument(arg));
			}
		}
		return builder.toString();
	}

	public static String list(Iterable<Matcher> iterable) {
		StringBuilder builder = new StringBuilder();
			for (Object arg : iterable) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(argument(arg));
			}
		return builder.toString();
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
