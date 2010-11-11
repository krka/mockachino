package se.mockachino;

import com.google.inject.TypeLiteral;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeLiteralTest {

    @Test
    public void testTypeLiteralDeepMock() {
        final List<Set> mock = Mockachino.mock(new TypeLiteral<List<Set>>() {}, Settings.fallback(Mockachino.DEEP_MOCK));
        final Set mock2 = mock.get(0);
        Mockachino.when(mock2.size()).thenReturn(123);
        assertEquals(123, mock.get(0).size());
    }

    @Test
    public void testTypeLiteralDeepMockFinalClass() {
        final List<String> mock = Mockachino.mock(new TypeLiteral<List<String>>() {}, Settings.fallback(Mockachino.DEEP_MOCK));
        final String mock2 = mock.get(0);
        assertTrue(mock2 == null);
    }

    @Test
    public void testTypeLiteral() {
        final TypeLiteral<List<Set>> typeLiteral = new TypeLiteral<List<Set>>() {};
        assertEquals(List.class, typeLiteral.getRawType());
        assertTrue(typeLiteral.getType() instanceof ParameterizedType);
        final ParameterizedType type = (ParameterizedType) typeLiteral.getType();
        assertEquals(Arrays.asList(Set.class), Arrays.asList(type.getActualTypeArguments()));
    }

    static interface ListSet extends List<Set> {}

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
}
