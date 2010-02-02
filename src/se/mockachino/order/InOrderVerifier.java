package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.verifier.VerificationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.order.InOrder;

import java.util.List;

public class InOrderVerifier extends VerificationHandler {
	private final MockData data;
	private InOrder inOrder;

	public InOrderVerifier(InOrder inOrder, MockData data) {
		super("InOrderVerifier");
		this.inOrder = inOrder;
		this.data = data;
	}
	
	@Override
	public void verify(Object o, MethodMatcher matcher) {
		List<MethodCall> calls = data.getCalls();
		for (MethodCall call : calls) {
			if (matcher.matches(call)) {
				if (inOrder.consume(call)) {
					int number = call.getCallNumber();
					if (number < inOrder.getCurrentCallNumber()) {
						String errorMessage = "Calls were out of order:\n";
						errorMessage += "Call to " + call + " occured before " + inOrder.getCurrentCall();
						throw new VerificationError(errorMessage);
					} else {
						inOrder.setCurrent(number, call);
						return;
					}
				}
			}
		}
		throw new VerificationError("No call found for " + matcher);
	}
}
