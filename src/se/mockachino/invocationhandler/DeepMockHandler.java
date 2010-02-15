package se.mockachino.invocationhandler;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.util.MockachinoMethod;

/**
 *  Do not use, does not work yet.
 */
@Deprecated
public class DeepMockHandler extends AbstractInvocationHandler {
	private final MockContext context;
	private final AbstractInvocationHandler delegate;

	public DeepMockHandler(MockContext context, AbstractInvocationHandler delegate) {
		super(null);
		this.context = context;
		this.delegate = delegate;
	}

	public Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		Class returnType = method.getReturnType();
		if (context.canMock(returnType)) {
			Object returnValue = context.mock(returnType, this);
			context.stubReturn(returnValue).onMethodWithAnyArgument(o, method);
			return returnValue;
		}
		return delegate.doInvoke(o, method, objects);
	}
}