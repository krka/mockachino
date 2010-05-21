package se.mockachino.matchers;

import se.mockachino.matchers.matcher.BasicMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.Formatting;

import java.lang.reflect.Array;

public class ArrayMatcher<T> extends BasicMatcher<T> {
    private final Matcher[] array;
    private final Class<T> componentType;
    private final boolean varArg;

    public ArrayMatcher(Matcher[] array, Class<T> componentType, boolean varArg) {
        this.array = array;
        this.componentType = componentType;
        this.varArg = varArg;
    }

    @Override
    protected String asString() {
        if (varArg) {
            return Formatting.join(", ", array);
        }
        return Formatting.argument(array);
    }

    @Override
    public boolean matches(Object value) {
        int n1 = array.length;
        if (value == null) {
            return n1 == 0;
        }
        if (!value.getClass().isArray()) {
            return false;
        }
        if (n1 != Array.getLength(value)) {
            return false;
        }

        for (int i = 0; i < n1; i++) {
            Object v = Array.get(value, i);
            if (!array[i].matches(v)) {
                return false;
            }

        }
        return true;
    }

    @Override
    public Class<T> getType() {
        return componentType;
    }
}
