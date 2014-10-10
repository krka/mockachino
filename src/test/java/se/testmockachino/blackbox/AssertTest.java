package se.testmockachino.blackbox;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.util.ArrayList;

public class AssertTest {
    @Test
    public void testAssertThrows() {
        ArrayList list = new ArrayList();
        Mockachino.assertThrows(IndexOutOfBoundsException.class).on(list).get(0);
    }

    @Test(expected = AssertionError.class)
    public void testAssertThrowsFailsWithWrongException() {
        ArrayList list = new ArrayList();
        Mockachino.assertThrows(ArrayIndexOutOfBoundsException.class).on(list).get(0);
    }

    @Test(expected = AssertionError.class)
    public void testAssertThrowsNoException() {
        ArrayList list = new ArrayList();
        list.add("");
        Mockachino.assertThrows(IndexOutOfBoundsException.class).on(list).get(0);
    }

}
