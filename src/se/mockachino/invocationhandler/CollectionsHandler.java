package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionsHandler implements CallHandler {

    private final CallHandler fallback;

    public CollectionsHandler(CallHandler fallback) {
        this.fallback = fallback;
    }

    @Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
        MockachinoMethod method = call.getMethod();
        Class returnType = method.getReturnType();

        Object returnValue = null;
        if (returnType == Object.class) {
            // Avoid returning collections for plain object
        } else if (returnType.isAssignableFrom(List.class)) {
            returnValue = Collections.synchronizedList(new ArrayList<Object>());
        } else if (returnType.isAssignableFrom(Map.class)) {
            returnValue = new ConcurrentHashMap();
        } else if (returnType.isAssignableFrom(Set.class)) {
            returnValue = Collections.synchronizedSet(new HashSet());
        }

        if (returnValue != null) {
            Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
            return returnValue;
        }

        return fallback.invoke(obj, call);
    }
}
