package se.mockachino.stub;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StubberTest {
	@Test
	public void testVoid() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn((Object) null).on(mock).clear();
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
			Mockachino.stubReturn((Object) null).on(mock).indexOf("");
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
		Mockachino.stubReturn(new InputStream() {
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

	@Test
	public void testStubAll() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubThrow(new IllegalStateException("unstubbed method call!")).onAnyMethod(mock);
		Mockachino.stubReturn(123).on(mock).size();

		assertEquals(123, mock.size());
		try {
			mock.get(123);
			fail();
		} catch (IllegalStateException e) {
			// Expected result
		}
	}

	@Test(expected = UsageError.class)
	public void testStubFails() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn("").on(mock).indexOf(null);
	}

	@Test(expected = UsageError.class)
	public void testStubFails2() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn("").on(mock).toArray();
	}
}
