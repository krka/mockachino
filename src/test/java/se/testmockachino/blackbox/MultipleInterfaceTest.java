package se.testmockachino.blackbox;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.stubReturn;

public class MultipleInterfaceTest {
    public interface Multi extends DataOutput, DataInput, Map {

    }

	@Test
	public void testMultipleInterfaces() throws IOException {
		Multi mock = Mockachino.mock(Multi.class);
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

}
