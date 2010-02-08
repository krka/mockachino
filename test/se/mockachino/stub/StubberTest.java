package se.mockachino.stub;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.fail;

public class StubberTest {
	@Test
	public void testVoid() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn(null).on(mock).clear();
	}

	@Test
	public void testVoidFail() {
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn("").on(mock).clear();
			fail("Should have failed");
		} catch (UsageError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPrimitiveOk() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn(1).on(mock).indexOf("");
	}

	@Test
	public void testPrimitiveOk2() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn(1).on(mock).indexOf("");
	}

	@Test
	public void testPrimitiveFail() {
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn(1L).on(mock).indexOf("");
			fail("Should have failed");
		} catch (UsageError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPrimitiveFail2() {
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn(null).on(mock).indexOf("");
			fail("Should have failed");
		} catch (UsageError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPrimitiveFail3() {
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn("").on(mock).indexOf("");
			fail("Should have failed");
		} catch (UsageError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testObjectOk() {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn("").on(mock).get(0);
	}

	@Test
	public void testObjectExtendsOk() {
		Process mock = Mockachino.mock(Process.class);
		Mockachino.stubReturn(new InputStream(){
			@Override
			public int read() throws IOException {
				throw new UnsupportedOperationException("NYI");
			}
		}).on(mock).getErrorStream();

	}

	@Test
	public void testObjectFail() {
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubReturn("").on(mock).iterator();
			fail("Should have failed");
		} catch (UsageError e) {
			e.printStackTrace();
		}
	}

}
