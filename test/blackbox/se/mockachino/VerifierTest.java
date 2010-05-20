package se.mockachino;

import org.junit.Test;
import se.mockachino.matchers.Matchers;
import se.mockachino.verifiers.SimpleVerifier;
import se.mockachino.verifiers.Verifier;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class VerifierTest {
	@Test
	public void testEmpty() {
		List mock = Mockachino.mock(List.class);

		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add("Hello");

		assertEquals(0, a.getMatches().size());
	}

	@Test
	public void testCount() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.add("Helly");
		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add(Matchers.regexp("Hell.*"));

		assertEquals(2,  a.getMatches().size());
	}

	@Test
	public void testJoin1() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		assertEquals(1, getCountHello(mock));
	}

	@Test
	public void testJoin2() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello2");
		assertEquals(0, getCountHello(mock));
	}

	@Test
	public void testJoin3() {
		List mock = Mockachino.mock(List.class);
		mock.remove("Hello");
		assertEquals(1, getCountHello(mock));
	}


	private int getCountHello(List mock) {
		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add("Hello");

		SimpleVerifier b = Mockachino.newVerifier();
		b.bind(mock).remove("Hello");

		Verifier orVerifier = a.or(b);
		int count = orVerifier.getMatches().size();
		return count;
	}

	@Test
	public void testJoinDuplicate() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");

		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add("Hello");

		SimpleVerifier b = Mockachino.newVerifier();
		b.bind(mock).add("Hello");

		Verifier orVerifier = a.or(b);
		assertEquals(1, orVerifier.getMatches().size());
	}

	@Test
	public void testOr() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.remove("Hello");

		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add("Hello");

		SimpleVerifier b = Mockachino.newVerifier();
		b.bind(mock).remove("Hello");

		Verifier orVerifier = a.or(b);
		assertEquals(2, orVerifier.getMatches().size());
	}

	@Test
	public void testAnd() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.add("Jello");

		SimpleVerifier a = Mockachino.newVerifier();
		a.bind(mock).add("Hello");

		SimpleVerifier b = Mockachino.newVerifier();
		b.bind(mock).add(Matchers.any(String.class));

		assertEquals(1, a.getMatches().size());
		assertEquals(2, b.getMatches().size());
		assertEquals(1, a.and(b).getMatches().size());
	}

}
