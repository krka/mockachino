package se.mockachino.invocationhandler;

import com.google.inject.TypeLiteral;
import se.mockachino.*;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

public class DeepMockHandler implements CallHandler {
	private final MockContext context;
	private final CallHandler delegate;

	public DeepMockHandler(MockContext context, CallHandler delegate) {
		this.context = context;
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
        final MockData<Object> data = Mockachino.getData(obj);
		MockachinoMethod method = call.getMethod();

        TypeLiteral typeLiteral = data.getTypeLiteral();
        TypeLiteral returnType = typeLiteral.getReturnType(method.getMethod());

        if (Mockachino.canMock(returnType.getRawType())) {
			Object returnValue = context.mock(returnType, Settings.fallback(this));
			Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
			return returnValue;
		}
		return delegate.invoke(obj, call);
	}
}