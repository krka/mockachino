package se.testmockachino;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.io.DataInput;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class DefaultReturnValuesTest {
	@Test
	public void testInt() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0, mock.readInt());
	}

	@Test
	public void testLong() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0, mock.readLong());
	}

	@Test
	public void testShort() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0, mock.readShort());
	}

	@Test
	public void testByte() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0, mock.readByte());
	}

	@Test
	public void testDouble() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0.0, mock.readDouble());
	}

	@Test
	public void testFloat() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(0.0f, mock.readFloat());
	}

	@Test
	public void testBoolean() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals(false, mock.readBoolean());
	}

	@Test
	public void testCharacter() throws IOException {
		DataInput mock = Mockachino.mock(DataInput.class);
		assertEquals('\0', mock.readChar());
	}
}
