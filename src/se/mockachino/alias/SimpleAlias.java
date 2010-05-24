package se.mockachino.alias;

import se.mockachino.MethodCall;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.BadUsageBuilder;
import se.mockachino.verifier.BadUsageHandler;
import se.mockachino.verifier.MatchingHandler;

import java.util.ArrayList;
import java.util.List;

public class SimpleAlias extends AbstractAlias {
	private static final BadUsageHandler BAD_USAGE_HANDLER = new BadUsageHandler(
			new BadUsageBuilder(
					"Incorrect usage. You can not chain calls when binding a verifier." +
					"You probably used verifier.bind(mock).method1().method2()."));

	private MethodMatcher methodMatcher;
	private Object mock;

	public <T> T bind(T mock) {
		if (this.mock != null) {
			multipleUsageError();
		}
		this.mock = mock;
		MatchingHandler handler = new MatchingHandler("Alias", mock.toString(), BAD_USAGE_HANDLER) {
			@Override
			protected void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
				if (SimpleAlias.this.methodMatcher != null) {
					multipleUsageError();
				}
				SimpleAlias.this.methodMatcher = matcher;
			}
		};
		MockData<T> data = Mockachino.getData(mock);
		return ProxyUtil.newProxy(data.getInterface(), handler);
	}

	private void multipleUsageError() {
		throw new UsageError("You can only bind a verifier once.");
	}

	@Override
	public List<MethodCall> getMatches() {
        if (methodMatcher == null) {
            throw new UsageError("Alias must be bound to a mock method");
        }
        List<MethodCall> res = new ArrayList<MethodCall>();
        MockData<Object> data = Mockachino.getData(mock);
        for (MethodCall call : data.getCalls()) {
            if (methodMatcher.matches(call)) {
                res.add(call);
            }
        }
        return res;
    }

}
