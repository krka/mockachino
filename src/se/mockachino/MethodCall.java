package se.mockachino;

import se.mockachino.util.Formatting;
import se.mockachino.util.MockachinoMethod;

import java.util.Arrays;

public class MethodCall {


	private static final Object[] NO_ARGS = new Object[0];


	private final MockachinoMethod method;
	private final Object[] args;

	public MethodCall(MockachinoMethod method, Object[] args) {
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

	public MockachinoMethod getMethod() {
		return method;
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
		return true;
	}
}
