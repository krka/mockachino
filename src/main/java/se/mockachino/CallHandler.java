package se.mockachino;

public interface CallHandler<T> {
	T invoke(Object obj, MethodCall<T> call) throws Throwable;
}
