package se.mockachino;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class CollectionsTest {
    private static interface Foo {
        Iterable<String> getIter(int x);
        Collection<String> getCollection(int x);
        List<String> getList(int x);
        Set<String> getSet(int x);
        Map<String, String> getMap(int x);
    }

    @Test
    public void testIterable() {
        Foo mock = Mockachino.mock(Foo.class);

        assertNotNull(mock);
        assertNotNull(mock.getIter(1));
        assertTrue(mock.getIter(1) == mock.getIter(1));
        assertFalse(mock.getIter(1) == mock.getIter(2));
    }

    @Test
    public void testCollection() {
        Foo mock = Mockachino.mock(Foo.class);

        assertNotNull(mock);
        assertNotNull(mock.getCollection(1));
        assertTrue(mock.getCollection(1) == mock.getCollection(1));
        assertFalse(mock.getCollection(1) == mock.getCollection(2));

        Collection<String> collection = mock.getCollection(1);
        collection.add("Hello");
        collection.add("World");
        assertEquals(2, collection.size());

        Collection<String> collection2 = mock.getCollection(1);
        assertEquals(2, collection2.size());

    }

    @Test
    public void testMap() {
        Foo mock = Mockachino.mock(Foo.class);

        assertNotNull(mock);
        assertNotNull(mock.getMap(1));
        assertTrue(mock.getMap(1) == mock.getMap(1));
        assertFalse(mock.getMap(1) == mock.getMap(2));

        Map<String, String> collection = mock.getMap(1);
        collection.put("Hello", "1");
        collection.put("World", "2");
        assertEquals(2, collection.size());

        Map<String, String> collection2 = mock.getMap(1);
        assertEquals(2, collection2.size());
    }

}
