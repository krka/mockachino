package se.mockachino.blackbox;

import org.junit.Before;
import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.annotations.Spy;
import se.mockachino.matchers.Matchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestSpyAnnotation {

	@Spy
	public List spy = new ArrayList();

	@Before
	public void before() {
		Mockachino.setupMocks(this);
	}

	@Test
	public void simpleTest() {
		verify(spy);

		assertEquals(0, spy.size());
		spy.add("Hello");
		spy.add("World");
		assertEquals(2, spy.size());
		assertEquals("Hello", spy.get(0));

		Mockachino.verifyExactly(2).on(spy).size();
		Mockachino.verifyExactly(2).on(spy).add(Matchers.any(Object.class));
		Mockachino.verifyExactly(1).on(spy).get(0);
	}

	private void verify(List mock) {
		assertNotNull(mock);
		assertNotNull(Mockachino.getData(mock));
	}
}