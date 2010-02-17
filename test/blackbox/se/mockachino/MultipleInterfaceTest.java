package se.mockachino;

import org.junit.Test;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.stubReturn;

public class MultipleInterfaceTest {
	@Test
	public void testMultipleInterfaces() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class, Settings.add(DataInput.class, Map.class));
		assertTrue(mock instanceof DataOutput);
		assertTrue(mock instanceof DataInput);
		assertTrue(mock instanceof Map);

		mock.write(123);
		Mockachino.verifyExactly(1).on(mock).write(123);

		Map asMap = (Map) mock;
		stubReturn("Hello").on(asMap).put("Hello", "World");
		assertEquals("Hello", asMap.put("Hello", "World"));
		Mockachino.verifyExactly(1).on(asMap).put("Hello", "World");
	}

	@Test
	public void testMultipleWithClassBase() throws IOException {
		OutputStream mock = Mockachino.mock(OutputStream.class, Settings.add(DataInput.class, Map.class));
		assertTrue(mock instanceof OutputStream);
		assertTrue(mock instanceof DataInput);
		assertTrue(mock instanceof Map);

		mock.write(123);
		Mockachino.verifyExactly(1).on(mock).write(123);

		Map asMap = (Map) mock;
		stubReturn("Hello").on(asMap).put("Hello", "World");
		assertEquals("Hello", asMap.put("Hello", "World"));
		Mockachino.verifyExactly(1).on(asMap).put("Hello", "World");
	}

}
