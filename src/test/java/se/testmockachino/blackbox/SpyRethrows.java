package se.testmockachino.blackbox;

import org.junit.Test;
import se.mockachino.Mockachino;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpyRethrows {
    public class Example {
        public void foo() throws IOException {
            throw new IOException("Hello world");
        }
    }
    @Test
    public void testSpyRethrows() throws Exception {
        Example spy = Mockachino.spy(new Example());
        try {
            spy.foo();
        } catch (IOException e) {
            assertEquals("Hello world", e.getMessage());
            return;
        }
        fail();

    }
}
