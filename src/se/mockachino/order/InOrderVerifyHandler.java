package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.util.Formatting;
import se.mockachino.verifier.MatchingHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.OrderingContext;

import java.util.List;

public class InOrderVerifyHandler extends MatchingHandler {
	private final MockData data;
	private final int min;
	private final OrderingContext orderingContext;

	public InOrderVerifyHandler(OrderingContext orderingContext, Object mock, MockData data, int min) {
		super("InOrderVerifyHandler", mock.toString());
		this.orderingContext = orderingContext;
		this.data = data;
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

		List<MethodCall> calls = data.getCalls();
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
		String errorMessage = "Expected " + Formatting.calls(min) + " to mock." + matcher + " but only got "
				+ Formatting.calls(count);
		if (lastCallNumber > 0) {
			errorMessage += " after mock." + lastCall + "\n\t\t" + lastCall.getStackTrace();
		}
		throw new VerificationError(errorMessage);
	}
}
