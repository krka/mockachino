package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.alias.Alias;
import se.mockachino.alias.SimpleAlias;
import se.mockachino.matchers.Matchers;

import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class SpyFinalTest {
	@Test
	public void testFinal() {
        CharSequence spy = Mockachino.spy("Hello world");
        spy.charAt(5);

        Mockachino.verifyOnce().on(spy).charAt(5);
    }

    @Test
    public void testAnonymous() throws Exception {
        Callable<String> spy = Mockachino.spy(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello world";
            }
        });

        spy.call();
        spy.call();
        spy.call();

        Mockachino.verifyExactly(3).on(spy).call();
    }


}
