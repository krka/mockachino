package se.mockachino.util;

import org.junit.Test;

import java.util.*;

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

		s = Formatting.join(", ", (Iterable) null);
		assertEquals("", s);

		s = Formatting.join(", ", (Object[]) null);
		assertEquals("", s);

    }

    @Test
    public void testVarargs() {
        String s = Formatting.join(true, ", ", 1, 2, new Object[]{7, 8});
        assertEquals("1, 2, 7, 8", s);

        s = Formatting.join(false, ", ", 1, 2, new Object[]{7, 8});
        assertEquals("1, 2, Object[]{7, 8}", s);
	}

    @Test
    public void testCollection() {
        String s = Formatting.join(", ", 1, 2, new ArrayList(Arrays.asList(4, 5, 6)));
        assertEquals("1, 2, [4, 5, 6]", s);

        s = Formatting.join(", ", 1, 2, new TreeSet(Arrays.asList(4, 5, 6)));
        assertEquals("1, 2, {4, 5, 6}", s);

        Map map = new HashMap();
        map.put("A", "a");
        map.put("B", "b");
        s = Formatting.join(", ", 1, 2, map);
        assertEquals("1, 2, (\"A\":\"a\", \"B\":\"b\")", s);
    }
}
