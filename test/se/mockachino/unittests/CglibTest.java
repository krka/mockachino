package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class CglibTest {
	@Test
	public void testSimple() {
		ArrayList mock = Mockachino.mock(ArrayList.class);
		mock.add("Hello");

		Mockachino.verifyOnce().on(mock).add("Hello");
	}

	@Test
	public void testFinal() {
		try {
			String mock = Mockachino.mock(String.class);
			mock.concat("Hello");

			Mockachino.verifyOnce().on(mock).concat("Hello");
			fail("Should not work");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
