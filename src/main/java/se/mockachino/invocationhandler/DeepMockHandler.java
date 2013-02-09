package se.mockachino.invocationhandler;

import com.googlecode.gentyref.GenericTypeReflector;
import se.mockachino.*;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.stub.MethodStub;
import se.mockachino.stub.MethodStubs;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Type;

public class DeepMockHandler implements CallHandler<Object> {
	private final CallHandler delegate;

	public DeepMockHandler(CallHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		final MockData<Object> data = Mockachino.getData(obj);
		MockachinoMethod method = call.getMethod();

		Type type = data.getTypeLiteral();

		final Type returnType = MockachinoMethod.getReturnType(type, method.getMethod());

		if (Mockachino.canMock(GenericTypeReflector.erase(returnType))) {
            synchronized (obj) {
                // Since this is lazily added, we need to make sure it hasn't already been added by another thread.
                MethodStubs stubs = data.getStubs(method);
                MethodStub stub = stubs.findMatch(call);
                if (stub != null) {
                    return stub.getAnswer().invoke(obj, call);
                }

                Object returnValue = Mockachino.mockType(returnType, Settings.fallback(this));
                Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
                return returnValue;
            }
        }
		return delegate.invoke(obj, call);
	}

}