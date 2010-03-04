package se.mockachino.proxy;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjenesisUtil {
    public static Object newInstance(Class<?> clazz) {
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator thingyInstantiator = objenesis.getInstantiatorOf(clazz);
        return thingyInstantiator.newInstance();
    }
}
