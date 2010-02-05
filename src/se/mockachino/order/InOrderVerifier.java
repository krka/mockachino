package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.util.Formatting;
import se.mockachino.verifier.VerificationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.InOrder;

import java.util.List;

public class InOrderVerifier extends VerificationHandler {
	private final MockData data;
	private final int min;
	private final InOrder inOrder;

	public InOrderVerifier(InOrder inOrder, Object mock, MockData data, int min) {
		super("InOrderVerifier", mock.toString());
		this.inOrder = inOrder;
		this.data = data;
		this.min = min;
	}
	
	@Override
	public void verify(Object o, MethodMatcher matcher) {
		if (min <= 0) {
			return;
		}

		MethodCall lastCall = inOrder.getCurrentCall();
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
					inOrder.setCurrent(call);
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
