package se.mockachino;

public interface CallHandler {
	Object invoke(Object obj, MethodCall call) throws Throwable;
}
