package se.mockachino.exceptions;

import se.mockachino.cleaner.StacktraceCleaner;

import java.util.List;

public class VerificationError extends RuntimeException {
	public VerificationError(String s) {
		super(s);
		List<StackTraceElement> elementList = StacktraceCleaner.getCleanStacktrace(this);
		setStackTrace(StacktraceCleaner.convert(elementList));
	}
}
