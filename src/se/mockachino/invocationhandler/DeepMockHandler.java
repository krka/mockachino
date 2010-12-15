package se.mockachino.invocationhandler;

import com.googlecode.gentyref.GenericTypeReflector;
import se.mockachino.*;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Type;

public class DeepMockHandler implements CallHandler {
	private final CallHandler delegate;

	public DeepMockHandler(CallHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		final MockData<Object> data = Mockachino.getData(obj);
		MockachinoMethod method = call.getMethod();

		Type type = data.getTypeLiteral();

		final Type returnType = GenericTypeReflector.getExactReturnType(method.getMethod(), type);

		if (Mockachino.canMock(GenericTypeReflector.erase(returnType))) {
			Object returnValue = Mockachino.mockType(returnType, Settings.fallback(this));
			Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
			return returnValue;
		}
		return delegate.invoke(obj, call);
	}

}