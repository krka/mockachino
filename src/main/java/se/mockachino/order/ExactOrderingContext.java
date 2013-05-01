package se.mockachino.order;

import se.mockachino.Invocation;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MatcherThreadHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Use this context to verify that mocks have been called in an exact predefined order.
 *
 * @author bjorn.ekryd
 * @since 2013-04-25
 */
public class ExactOrderingContext {
    /** Contains all mocks that should be verified in this context */
    private final List<Object> verifiableContextMocks;
    
    /** Contains all invocations that have been performed for the mocks */
    private final List<Invocation> allInvocations;
    
    /** This counter keeps track of the order which the verifications are called */
    private AtomicInteger verficationCallCounter = new AtomicInteger(0);

    /**
     * Create a context. Supply all mocks that you want to verify.
     *
     * @param mocks these mocks will be registered for exact order verification
     */
    public ExactOrderingContext(Object... mocks) {
        verifiableContextMocks = new ArrayList<Object>(Arrays.asList(mocks));
        allInvocations = getAllInvocations(mocks);
    }

    private List<Invocation> getAllInvocations(Object[] mocks) {
        TreeSet<Invocation> allInvocations = new TreeSet<Invocation>(Invocation.COMPARATOR);
        for (Object mock : mocks) {
            MockData<Object> data = Mockachino.getData(mock);
            Iterable<Invocation> invocations = data.getInvocations();
            for (Invocation invocation : invocations) {
                allInvocations.add(invocation);
            }
        }
        return new ArrayList<Invocation>(allInvocations);
    }

    /** Verify that a mock method have been called once */
    public ExactOrderVerify verifyOnce() {
        return verify(1);
    }
    
    /** Verify that a mock method have been called an exact number of times */    
    public ExactOrderVerify verify(int nrOfInvocations) {
        MatcherThreadHandler.assertEmpty();
        return new ExactOrderVerify(this, verficationCallCounter.getAndIncrement(), nrOfInvocations);
    }

    /** Verify that no other method calls have been performed besides the ones that already have been verified */
    public void verifyNoMoreInvocations() {
        MatcherThreadHandler.assertEmpty();

        if (verficationCallCounter.get() < allInvocations.size()) {
            String errorMessage = "Expected no more calls but got ";
            errorMessage += "\nmethod pattern:\n" + getInvocation(verficationCallCounter.get()).toString() + "\n";
            throw new VerificationError(errorMessage);
        }
    }

    Invocation getInvocation(int invocationIndex) {
        if (invocationIndex < 0) {
            return Invocation.NULL;
        }
        if (invocationIndex >= allInvocations.size()) {
            throw new VerificationError("Method was not invoked");
        }
        return allInvocations.get(invocationIndex);
    }
    
    boolean doesMockExistInContext(Object mock) {
        return verifiableContextMocks.contains(mock);
    }

}
