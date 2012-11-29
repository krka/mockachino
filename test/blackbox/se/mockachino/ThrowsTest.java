package se.mockachino;

import org.junit.Test;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.Matchers;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import static junit.framework.Assert.*;
import static se.mockachino.Mockachino.assertThrows;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.stubThrow;

public class ThrowsTest {
  @Test
  public void testCorrectStacktrace() {
    List mock = Mockachino.mock(List.class);
    Mockachino.when(mock.size()).thenThrow(new RuntimeException());

    for (int i = 0; i < 10; i++) {
      // Do some work, to make sure expected will have a different line than the one above.
    }

    RuntimeException expected = new RuntimeException();
    try {
      mock.size();
    } catch (Exception e) {
      StackTraceElement[] exp = expected.getStackTrace();
      StackTraceElement[] act = e.getStackTrace();

      assertEquals(exp[0].getClassName(), act[0].getClassName());
      assertEquals(exp[0].getLineNumber() + 2, act[0].getLineNumber());
    }

  }

  @Test
  public void testNewExceptions() {
    List mock = Mockachino.mock(List.class);
    IOException cause = new IOException();
    RuntimeException exception = new RuntimeException("Hello world", cause);
    Exception exception2 = null;
    Mockachino.when(mock.size()).thenThrow(exception);
    try {
      mock.size();
      fail();
    } catch (Exception e) {
      assertEquals(cause, e.getCause());
      assertEquals("Hello world", e.getMessage());
      assertFalse(exception == e);
      exception2 = e;
    }
    try {
      mock.size();
      fail();
    } catch (Exception e) {
      assertFalse(e == exception2);
    }
  }

  @Test(expected = UsageError.class)
  public void validThrow() {
    List mock = Mockachino.mock(List.class);
    Mockachino.when(mock.size()).thenThrow(new IOException());
  }

  @Test
  public void testCheckedException() throws IOException {
    Socket mock = Mockachino.mock(Socket.class);
    Mockachino.stubThrow(new IOException()).on(mock).connect(Matchers.any(SocketAddress.class));
  }

  @Test(expected = UsageError.class)
  public void testCheckedExceptionFail() throws IOException {
    Socket mock = Mockachino.mock(Socket.class);
    Mockachino.stubThrow(new ClassNotFoundException()).on(mock).connect(Matchers.any(SocketAddress.class));
  }

  @Test
  public void testExceptionClasses() {
    helper(new RuntimeException("Hello world", new IOException()));
    helper(new IllegalArgumentException("Hello world", new IOException()));
    helper(new Error("Hello world", new IOException()));
    helper(new StackOverflowError("Hello world"));
    helper(new UsageError("Hello world"));
  }

  private void helper(Throwable input) {
    List mock = Mockachino.mock(List.class);
    Mockachino.when(mock.size()).thenThrow(input);
    try {
      mock.size();
      fail();
    } catch (Throwable e) {
      assertEquals(input.getCause(), e.getCause());
      assertEquals(input.getMessage(), e.getMessage());
    }
  }

  @Test
  public void testNonStandardConstructor() throws Exception {
    TestClass testClass = mock(TestClass.class);
    stubThrow(new TestException(100)).on(testClass).doIt();
    assertThrows(TestException.class).on(testClass).doIt();
  }

  public interface TestClass {
    void doIt() throws TestException;
  }

  public class TestException extends Exception {
    public TestException(int val) {
    }
  }
}
