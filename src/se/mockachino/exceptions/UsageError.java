package se.mockachino.exceptions;

import se.mockachino.cleaner.StacktraceCleaner;

public class UsageError extends RuntimeException {
	public UsageError(String s) {
		super(s);
		StacktraceCleaner.cleanError(this);
	}
}
