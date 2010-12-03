package se.mockachino;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ThrowsTest {
	@Test
	public void testCorrectStacktrace() {
		List mock = Mockachino.mock(List.class);
		Mockachino.when(mock.size()).thenThrow(new RuntimeException());

		for (int i = 0; i < 10; i++) {
			// Do some work, to make sure f will have a different line than the one above.
		}

		RuntimeException f = new RuntimeException();
		try {
			mock.size();
		} catch (Exception e) {
			assertEquals(
					f.getStackTrace()[0].getLineNumber() + 2,
					e.getStackTrace()[0].getLineNumber());
		}

	}
}
