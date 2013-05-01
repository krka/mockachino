package se.mockachino.order;

import se.mockachino.Invocation;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.BadUsageBuilder;
import se.mockachino.verifier.BadUsageHandler;
import se.mockachino.verifier.MatchingHandler;
import se.mockachino.verifier.Reporter;

public class ExactOrderVerifyHandler extends MatchingHandler {
    public static final BadUsageHandler BAD_USAGE_HANDLER = new BadUsageHandler(
            new BadUsageBuilder(
                    "Incorrect usage. You can not chain calls when verifying a deep mock. " +
                            "You probably used verify().on(mock).method1().method2(). " +
                            "Correct usage is verify().on(mock.method1()).method2()"));

    private final int invocationIndex;
    private final int nrOfInvocations;
    private final ExactOrderingContext orderingContext;

    public ExactOrderVerifyHandler(ExactOrderingContext orderingContext, Object mock, int invocationIndex, int nrOfInvocations) {
        super("ExactOrderVerifyHandler", Mockachino.getData(mock).getName(), BAD_USAGE_HANDLER, Mockachino.getData(mock).getTypeLiteral());
        this.orderingContext = orderingContext;
        this.invocationIndex = invocationIndex;
        this.nrOfInvocations = nrOfInvocations;
    }

    @Override
    public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
        if (nrOfInvocations <= 0) {
            return;
        }

        int count;
        boolean failed = false;

        for (count = 0; count < nrOfInvocations && !failed; count++) {
            Invocation currentInvocation = orderingContext.getInvocation(invocationIndex + count);
            if (!matcher.matches(currentInvocation.getMethodCall())) {
                failed = true;
            }
            if (!matcher.getMethod().getType().equals(currentInvocation.getMethodCall().getMethod().getType())) {
                failed = true;
            }

        }
        if (!failed) {
            return;
        }

        Invocation lastCall = orderingContext.getInvocation(invocationIndex + count - 1);
        String errorMessage = new Reporter(count, nrOfInvocations, nrOfInvocations).getErrorLine();
        errorMessage += "\nMethod pattern:\n" + matcher.toString() + "\n";
        if (1 > 0) {
            errorMessage += "\nAfter:\n" + lastCall + "\n" + lastCall.getStackTraceString();
        }
        throw new VerificationError(errorMessage);
    }
}
