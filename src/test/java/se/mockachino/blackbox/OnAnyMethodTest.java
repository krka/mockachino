package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.exceptions.VerificationError;

import static org.junit.Assert.fail;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyExactly;

public class OnAnyMethodTest {
	public interface FooInterface {
		void method2(String string);
	}

	@Test
	public void matchAnyMethodCallExactly() {
		FooInterface fooInterface = mock(FooInterface.class);
		callTwiceWithNullArgument(fooInterface);
		verifyExactly(2).onAnyMethod(fooInterface);
	}

	@Test
	public void failVerificationOfOnAnyMethodWithNullArguments() {
		FooInterface fooInterface = mock(FooInterface.class);
		callTwiceWithNullArgument(fooInterface);
		try {
			verifyExactly(1).onAnyMethod(fooInterface);
			fail("VerifyExactly(1) should throw VerificationError");
		} catch (VerificationError e) {

		}
	}

	public static void callTwiceWithNullArgument(FooInterface fooInterface) {
		fooInterface.method2(null);
		fooInterface.method2(null);
	}


}
