package se.mockachino.matchers;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArgumentCatcherTest {
	@Test
	public void testSimple() {
		List mock = Mockachino.mock(List.class);
		ArgumentCatcher<Integer> catcher = new ArgumentCatcher<Integer>(new AnyMatcher(Integer.class));
		mock.get(123);
		Mockachino.verify(mock).get(Matchers.matcher(catcher));
		assertEquals(Integer.valueOf(123), catcher.getValue());
	}
}
