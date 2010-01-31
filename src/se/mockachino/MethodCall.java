package se.mockachino;

import se.mockachino.util.Formatting;
import se.mockachino.cleaner.StacktraceCleaner;

import java.lang.reflect.Method;
import java.util.List;

public class MethodCall {
	private final Method method;
	private final int callNumber;
	private final Object[] args;
	private final List<StackTraceElement> stacktrace;

	public MethodCall(Method method, Object[] args, int callNumber, List<StackTraceElement> stacktrace) {
		this.method = method;
		this.callNumber = callNumber;
		this.args = args;
		this.stacktrace = stacktrace;
	}

	@Override
	public String toString() {
		return method.getName() + "(" + Formatting.list(args) + ");";
	}

	public Object[] getArguments() {
		return args;
	}

	public Method getMethod() {
		return method;
	}

	public int getCallNumber() {
		return callNumber;
	}

	public String getStackTrace() {
		return Formatting.toString(StacktraceCleaner.convert(stacktrace));
	}
}
