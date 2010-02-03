package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.listener.MethodCallListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodListenerTest {
	@Test
	public void testSimple() {
		List mock = Mockachino.mock(List.class);

		final List<String> list = new ArrayList<String>();
		MethodCallListener listener = new MethodCallListener() {
			@Override
			public void listen(Object proxy, MethodCall call) {
				list.add(call.toString());
			}
		};
		Mockachino.listenWith(listener).on(mock).get(123);
		
		mock.get(123);
		mock.get(124);
		assertEquals(1, list.size());
		assertEquals("get(123)", list.get(0));
	}
}
