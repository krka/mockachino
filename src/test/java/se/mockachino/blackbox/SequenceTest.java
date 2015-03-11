package se.mockachino.blackbox;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.when;

public class SequenceTest {

	@Test
	public void testMultipleReturns() {

		List mock = mock(List.class);
		when(mock.get(123)).thenReturn("A", "B", "C", null);

		assertEquals("A", mock.get(123));
		assertEquals(null, mock.get(456));
		assertEquals("B", mock.get(123));
		assertEquals(null, mock.get(456));
		assertEquals("C", mock.get(123));
		assertEquals(null, mock.get(456));
		assertEquals(null, mock.get(123));
		assertEquals(null, mock.get(456));
		assertEquals(null, mock.get(123));
		assertEquals(null, mock.get(456));
	}
}
