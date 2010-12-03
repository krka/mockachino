package se.mockachino;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockSettingsTest {
	@Test
	public void testSetName() {
		List mock = Mockachino.mock(List.class, Settings.name("MyMock"));
		assertEquals("MyMock", mock.toString());
		Mockachino.stubReturn("New name").on(mock).toString();
		assertEquals("New name", mock.toString());

		Mockachino.stubReturn((Object) null).on(mock).toString();
		assertEquals("MyMock", mock.toString());
	}

	@Test
	public void testSetNameAndReset() {
		List mock = Mockachino.mock(List.class, Settings.name("MyMock"));
		assertEquals("MyMock", mock.toString());
		Mockachino.stubReturn("New name").on(mock).toString();
		assertEquals("New name", mock.toString());

		Mockachino.getData(mock).resetStubs();
		assertEquals("MyMock", mock.toString());
	}

}
