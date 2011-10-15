package se.mockachino;

import se.mockachino.util.MockachinoMethod;

public class VerifyableCallHandlerWrapper implements VerifyableCallHandler {
    private final CallHandler callHandler;

    public VerifyableCallHandlerWrapper(CallHandler callHandler) {
        this.callHandler = callHandler;
    }

    @Override
    public void verify(MockachinoMethod method) {
    }

    @Override
    public Object invoke(Object obj, MethodCall call) throws Throwable {
        return callHandler.invoke(obj, call);
    }
}
