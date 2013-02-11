package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Primitives;
import se.mockachino.util.MockachinoMethod;

public class PrimitiveInvocationHandler implements CallHandler {
	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
    MockachinoMethod method = call.getMethod();
    if (method.equals(MockachinoMethod.EQUALS)) {
      return obj == call.getArguments()[0];
    }
    if (method.equals(MockachinoMethod.HASHCODE)) {
      return System.identityHashCode(obj);
    }
		return Primitives.forType(call.getMethod().getReturnType());
	}
}
