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
			// Do some work, to make sure expected will have a different line than the one above.
		}

		RuntimeException expected = new RuntimeException();
		try {
			mock.size();
		} catch (Exception e) {
			StackTraceElement[] exp = expected.getStackTrace();
			StackTraceElement[] act = e.getStackTrace();
			
			assertEquals(exp[0].getClassName(), act[0].getClassName());
			assertEquals(exp[0].getLineNumber() + 2, act[0].getLineNumber());
		}

	}
}
