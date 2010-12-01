package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.Collections;
import java.util.List;

class MatchAny implements MethodMatcher
{

    public static final MatchAny INSTANCE = new MatchAny();
    
    @Override
    public boolean matches(MethodCall methodCall)
    {
        return true;
    }

    @Override
    public List<Matcher> getArgumentMatchers()
    {
        return Collections.EMPTY_LIST;
    }

    @Override
    public MockachinoMethod getMethod()
    {
        return MockachinoMethod.NULL;
    }
}
