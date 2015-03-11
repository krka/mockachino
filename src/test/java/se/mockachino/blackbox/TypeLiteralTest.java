package se.mockachino.blackbox;

import com.googlecode.gentyref.GenericTypeReflector;
import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.Settings;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TypeLiteralTest {

	@Test
	public void testTypeLiteralDeepMock() {
		final List<Set> mock = Mockachino.mock(new TypeToken<List<Set>>() {
    }, Settings.fallback(Mockachino.DEEP_MOCK));
		final Set mock2 = mock.get(0);
		Mockachino.when(mock2.size()).thenReturn(123);
		assertEquals(123, mock.get(0).size());
	}

	@Test
	public void testTypeLiteralDeepMockFinalClass() {
		final List<String> mock = Mockachino.mock(new TypeToken<List<String>>() {
		}, Settings.fallback(Mockachino.DEEP_MOCK));
		final String mock2 = mock.get(0);
		assertTrue(mock2 == null);
	}

	@Test
	public void testTypeLiteral() {
		final TypeToken<List<Set>> typeLiteral = new TypeToken<List<Set>>() {
		};
		final Type type2 = typeLiteral.getType();
		assertEquals(List.class, GenericTypeReflector.erase(type2));
		assertTrue(type2 instanceof ParameterizedType);
		final ParameterizedType type = (ParameterizedType) type2;
		assertEquals(Arrays.asList(Set.class), Arrays.asList(type.getActualTypeArguments()));
	}

	static interface ListSet extends List<Set> {
	}

	@Test
	public void testDeepMockWithClass() {
		final ListSet mock = Mockachino.mock(ListSet.class, Settings.fallback(Mockachino.DEEP_MOCK));
		final Set mock2 = mock.get(0);
		final Set mock3 = mock.get(0);
		assertNotNull(mock2);
		assertNotNull(mock3);
		assertTrue(mock2.size() == 0);
		assertEquals(mock2, mock3);
	}

	@Test
	public void testDeeperMock() {
		List<Set<List<Runnable>>> mock = Mockachino.mock(new TypeToken<List<Set<List<Runnable>>>>() {
		}, Settings.fallback(Mockachino.DEEP_MOCK));
		final Set<List<Runnable>> mock2 = mock.get(0);
		final List<Runnable> mock3 = mock2.iterator().next();
		final Runnable mock4 = mock3.get(0);
		assertTrue(mock4 != null);
	}

}
