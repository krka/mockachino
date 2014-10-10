package se.testmockachino.blackbox;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChainedReturnTest {
	@Test
	public void testWhen() {
		List<String> mock = Mockachino.mock(List.class);

        Mockachino.when(mock.get(0)).thenReturn("Hello").thenReturn("World", "Goodbye").thenReturn("Cruel").thenAnswer(new CallHandler() {
            @Override
            public Object invoke(Object obj, MethodCall call) throws Throwable {
                return "World2";
            }
        }).thenThrow(new RuntimeException("EOF"));

        assertEquals("Hello", mock.get(0));
        assertEquals("World", mock.get(0));
        assertEquals("Goodbye", mock.get(0));
        assertEquals("Cruel", mock.get(0));
        assertEquals("World2", mock.get(0));
        Mockachino.assertThrows(RuntimeException.class).on(mock).get(0);
    }

	@Test
	public void testStub() {
		List<String> mock = Mockachino.mock(List.class);

        Mockachino.stubReturn("Hello").thenReturn("World", "Goodbye").thenReturn("Cruel").thenAnswer(new CallHandler() {
            @Override
            public Object invoke(Object obj, MethodCall call) throws Throwable {
                return "World2";
            }
        }).thenThrow(new RuntimeException("EOF")).on(mock).get(0);

        assertEquals("Hello", mock.get(0));
        assertEquals("World", mock.get(0));
        assertEquals("Goodbye", mock.get(0));
        assertEquals("Cruel", mock.get(0));
        assertEquals("World2", mock.get(0));
        Mockachino.assertThrows(RuntimeException.class).on(mock).get(0);
    }

    @Test(expected = UsageError.class)
    public void testStubWrongType() {
        List<String> mock = Mockachino.mock(new TypeToken<List<String>>(){});

        Mockachino.stubReturn("Hello").thenReturn(1234).on(mock).get(0);
    }

	@Test
	public void testWhenRepeatLast() {
		List<String> mock = Mockachino.mock(List.class);

        Mockachino.when(mock.get(0)).thenReturn("Hello").thenReturn("World", "Goodbye");
        assertEquals("Hello", mock.get(0));
        assertEquals("World", mock.get(0));
        assertEquals("Goodbye", mock.get(0));
        assertEquals("Goodbye", mock.get(0));
        assertEquals("Goodbye", mock.get(0));
	}
}
