package se.mockachino;

import se.mockachino.util.MockachinoMethod;

public interface VerifyableCallHandler extends CallHandler {
    void verify(MockachinoMethod method);
}
