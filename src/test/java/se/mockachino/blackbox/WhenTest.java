package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.matchers.Matchers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.anyInt;

public class WhenTest {
	@Test
	public void testWhen() {
		List mock = mock(List.class);
		when(mock.size()).thenReturn(123);
		verifyNever().on(mock).size();
		assertEquals(123, mock.size());

	}

	@Test
	public void testWhenWithMatcher() {
		List mock = mock(List.class);

		when(mock.get(anyInt())).thenReturn("Hello world");
		verifyNever().on(mock).get(0);
		verifyNever().on(mock).get(anyInt());
		assertEquals("Hello world", mock.get(1));
		assertEquals("Hello world", mock.get(2));
		assertEquals("Hello world", mock.get(3));
		assertEquals("Hello world", mock.get(4));
	}

	@Test
	public void testInlined() {
		// Note, currently this test doesn't work in Mockito.

		List mock = mock(List.class);
		List mock2 = mock(List.class);

		when(mock2.size()).thenReturn(123);

		when(mock.get(456)).thenReturn(mock2.size());

		verifyNever().on(mock).get(Matchers.anyInt());
		verifyOnce().on(mock2).size();

		assertEquals(123, mock2.size());
		assertEquals(123, mock.get(456));
	}

}
