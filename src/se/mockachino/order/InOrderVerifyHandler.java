package se.mockachino.order;

import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.verifier.MatchingHandler;
import se.mockachino.verifier.Reporter;

public class InOrderVerifyHandler extends MatchingHandler {
	private final Iterable<MethodCall> calls;
	private final int min;
	private final OrderingContext orderingContext;

	public InOrderVerifyHandler(OrderingContext orderingContext, Object mock, Iterable<MethodCall> calls, int min) {
		super("InOrderVerifyHandler", mock.toString());
		this.orderingContext = orderingContext;
		this.calls = calls;
		this.min = min;
	}

	@Override
	public void match(Object o, MethodMatcher matcher) {
		if (min <= 0) {
			return;
		}

		MethodCall lastCall = orderingContext.getCurrentCall();
		int lastCallNumber = lastCall.getCallNumber();

		int count = 0;

		for (MethodCall call : calls) {
			int number = call.getCallNumber();

			// Skip already visited calls
			if (number <= lastCallNumber) {
				continue;
			}

			if (matcher.matches(call)) {
				count++;
				if (count >= min) {
					orderingContext.setCurrent(call);
					return;
				}
			}
		}
		String errorMessage = new Reporter(count, min, Integer.MAX_VALUE).getErrorLine();
		errorMessage += "\nMethod pattern:\n"  + matcher.toString() + "\n";
		if (lastCallNumber > 0) {
			errorMessage += "\nAfter:\n" + lastCall + "\n" + lastCall.getStackTraceString();
		}
		throw new VerificationError(errorMessage);
	}
}
