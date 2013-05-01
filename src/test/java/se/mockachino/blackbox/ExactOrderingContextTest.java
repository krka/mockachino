package se.mockachino.blackbox;

import org.junit.Before;
import org.junit.Test;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.order.ExactOrderingContext;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;

public class ExactOrderingContextTest {

    Service1 s1 = mock(Service1.class);
    Service2 s2 = mock(Service2.class);
    Service2 s3 = mock(Service2.class);

    BlackBox blackBox = new BlackBox(s1, s2);

    @Before
    public void setup() {
        StacktraceCleaner.ENABLED = false;

        // Execute business method
        long result = blackBox.doSomething();
        assertEquals(42, result);
    }

    @Test
    public void doNothingAndZeroInvocationsShouldVerifyOk() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s3);
        orderingContext.verify(0).on(s3).methodB();
        orderingContext.verifyNoMoreInvocations();
    }

    @Test
    public void verifyInvocationOnNotActiveMockShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s3);
        orderingContext.verify(10).on(s3).methodB();
        orderingContext.verifyNoMoreInvocations();
    }

    @Test
    public void exactOrderingShouldWork() {
        // Verify that no side effects occurred 
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s1).methodA();
        orderingContext.verify(1).on(s2).methodB();
        orderingContext.verifyNoMoreInvocations();
    }

    @Test(expected = VerificationError.class)
    public void wrongVerificationMethodShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s1).methodA();

        //Wrong method verification
        orderingContext.verify(1).on(s2).methodA();
    }

    @Test(expected = VerificationError.class)
    public void wrongVerificationMockShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s1).methodA();

        //Wrong mock verification
        orderingContext.verify(1).on(s1).methodB();
    }

    @Test(expected = VerificationError.class)
    public void wrongNumberOfVerificationsShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(2).on(s1).methodA();
    }

    @Test(expected = VerificationError.class)
    public void tooManyVerificationMethodsShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s1).methodA();
        orderingContext.verify(1).on(s2).methodB();
        orderingContext.verify(1).on(s2).methodA();
    }

    @Test(expected = VerificationError.class)
    public void tooFewVerificationMethodsShouldFail() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s1).methodA();
        orderingContext.verifyNoMoreInvocations();
    }

    @Test(expected = IllegalArgumentException.class)
    public void mockOutsideContextShouldThrowException() {
        ExactOrderingContext orderingContext = new ExactOrderingContext(s1, s2);
        orderingContext.verify(1).on(s3).methodA();
    }

    private static interface Service1 {
        void methodA();

        void methodB();
    }

    private static interface Service2 {
        void methodB();

        void methodA();
    }

    private class BlackBox {
        private final Service1 s1;
        private final Service2 s2;

        public BlackBox(Service1 s1, Service2 s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        public long doSomething() {
            s1.methodA();
            s2.methodB();
            return 42;
        }
    }

}
