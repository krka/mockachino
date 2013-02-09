package se.mockachino;

import se.mockachino.util.MockachinoMethod;

public class VerifyableCallHandlerWrapper<T> implements VerifyableCallHandler<T> {
    private final CallHandler<T> callHandler;

    public VerifyableCallHandlerWrapper(CallHandler<T> callHandler) {
        this.callHandler = callHandler;
    }

    @Override
    public void verify(MockachinoMethod<T> method) {
    }

    @Override
    public T invoke(Object obj, MethodCall call) throws Throwable {
        return callHandler.invoke(obj, call);
    }
}
