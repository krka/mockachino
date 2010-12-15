package se.mockachino;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodListenerTest {
	@Test
	public void testSimple() {
		List mock = Mockachino.mock(List.class);

		final List<String> list = new ArrayList<String>();
		CallHandler listener = new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) {
				list.add(call.toString());
				return null;
			}
		};
		Mockachino.observeWith(listener).on(mock).get(123);

		mock.get(123);
		mock.get(124);
		assertEquals(1, list.size());
		assertEquals("get(123)", list.get(0));
	}
}
