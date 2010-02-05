package se.mockachino.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FormattingTest {
	@Test
	public void testJoin() {
		String s = Formatting.join(", ", Arrays.asList("Hello", "World", "Foo", "Bar"));
		assertEquals("\"Hello\", \"World\", \"Foo\", \"Bar\"", s);
		s = Formatting.join(", ", Arrays.asList("Hello"));
		assertEquals("\"Hello\"", s);
		s = Formatting.join(", ", Arrays.asList());
		assertEquals("", s);

		s = Formatting.join(", ", "Hello", "World", "Foo", "Bar");
		assertEquals("\"Hello\", \"World\", \"Foo\", \"Bar\"", s);
		s = Formatting.join(", ", "Hello");
		assertEquals("\"Hello\"", s);
		s = Formatting.join(", ");
		assertEquals("", s);

		s = Formatting.join(", ", null, null, 1, 2);
		assertEquals("null, null, 1, 2", s);

	}
}
