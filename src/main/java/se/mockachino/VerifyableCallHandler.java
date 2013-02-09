package se.mockachino;

import se.mockachino.util.MockachinoMethod;

public interface VerifyableCallHandler<T> extends CallHandler<T> {
    void verify(MockachinoMethod<T> method);
}
