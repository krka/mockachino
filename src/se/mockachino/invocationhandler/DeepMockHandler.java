package se.mockachino.invocationhandler;

import com.googlecode.gentyref.GenericTypeReflector;
import se.mockachino.*;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

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

        Type type = data.getTypeLiteral();

        final Type returnType = getReturnType(type, method.getMethod());

        if (Mockachino.canMock(GenericTypeReflector.erase(returnType))) {
			Object returnValue = context.mockType(returnType, Settings.fallback(this));
			Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
			return returnValue;
		}
		return delegate.invoke(obj, call);
	}

    private Type getReturnType(Type type, Method method)
    {
        // Workaround for bug in gentyref
        if (method.getDeclaringClass() == Object.class) {
            return method.getReturnType();
        }
        return  GenericTypeReflector.getExactReturnType(method, type);
    }
}