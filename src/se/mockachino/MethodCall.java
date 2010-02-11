package se.mockachino;

import se.mockachino.util.Formatting;
import se.mockachino.util.MockachinoMethod;

import java.util.Arrays;

public class MethodCall {
	private final MockachinoMethod method;
	private final int callNumber;
	private final Object[] args;
	private final StackTraceElement[] stacktrace;

	public MethodCall(MockachinoMethod method, Object[] args, int callNumber, StackTraceElement[] stacktrace) {
		this.method = method;
		this.callNumber = callNumber;
		this.args = args;
		this.stacktrace = stacktrace;
	}

	@Override
	public String toString() {
		return method.getName() + "(" + Formatting.list(args) + ")";
	}

	public Object[] getArguments() {
		return args;
	}

	public MockachinoMethod getMethod() {
		return method;
	}

	public int getCallNumber() {
		return callNumber;
	}

	public String getStackTraceString() {
		return Formatting.toString(stacktrace);
	}

	public String getStackTraceString(int maxLines) {
		return Formatting.toString(stacktrace, maxLines);
	}

	public boolean equals(MethodCall other) {
		if (other == null) {
			return false;
		}
		if (!method.equals(other.method)) {
			return false;
		}
		if (!Arrays.equals(args, other.args)) {
			return false;
		}
		if (stacktrace.length == 0 && other.stacktrace.length == 0) {
			return true;
		}
		return stacktrace[0].equals(other.stacktrace[0]);
	}

	public StackTraceElement[] getStackTrace() {
		return stacktrace;
	}
}
