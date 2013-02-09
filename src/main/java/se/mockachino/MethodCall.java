package se.mockachino;

import se.mockachino.util.Formatting;
import se.mockachino.util.MockachinoMethod;

import java.util.Arrays;

public class MethodCall<T> {


	private static final Object[] NO_ARGS = new Object[0];


	private final MockachinoMethod<T> method;
	private final Object[] args;

	public MethodCall(MockachinoMethod<T> method, Object[] args) {
		this.method = method;
		if (args == null) {
			args = NO_ARGS;
		}
		this.args = args;
	}

	@Override
	public String toString() {
		return method.getName() + "(" + Formatting.list(args, method.getMethod().isVarArgs()) + ")";
	}

	public Object[] getArguments() {
		return args;
	}

	public MockachinoMethod<T> getMethod() {
		return method;
	}

	public boolean equals(MethodCall<T> other) {
		if (other == null) {
			return false;
		}
		if (!method.equals(other.method)) {
			return false;
		}
		if (!Arrays.equals(args, other.args)) {
			return false;
		}
		return true;
	}
}
