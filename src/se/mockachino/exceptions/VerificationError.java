package se.mockachino.exceptions;

import se.mockachino.cleaner.StacktraceCleaner;

public class VerificationError extends RuntimeException {
	public VerificationError(String s) {
		super(s);
		StacktraceCleaner.cleanError(this);
	}

    public VerificationError(String s, Exception exception) {
        super(s, exception);
        StacktraceCleaner.cleanError(this);
    }
}
