package se.mockachino.verifier;

import se.mockachino.util.Formatting;

public class Reporter {
	private final int actual;
	private final int minCalls;
	private final int maxCalls;

	public Reporter(int actual, int minCalls, int maxCalls) {
		this.actual = actual;
		this.minCalls = minCalls;
		this.maxCalls = maxCalls;
	}

	public boolean isOk() {
		return minCalls <= actual && actual <= maxCalls;
	}

	public String getErrorLine() {
		// verifyNever()
		if (maxCalls <= 0) {
			if (actual > 0) {
				return "Expected no calls but got " + Formatting.calls(actual);
			}
			return null;
		}

		boolean exact = minCalls == maxCalls;
		if (actual < minCalls) {
			return tooFew(actual, minCalls, exact);
		}

		if (actual > maxCalls) {
			return tooMany(actual, maxCalls, exact);
		}
		return null;
	}

	private String tooFew(int actual, int minCalls, boolean exact) {
		String s = exact ? "" : "at least ";
		if (actual == 0) {
			return "Expected " + s + Formatting.calls(minCalls) + " but got " + Formatting.calls(actual);
		}
		return "Expected " + s + Formatting.calls(minCalls) + " but only got " + Formatting.calls(actual);
	}

	private String tooMany(int actual, int maxCalls, boolean exact) {
		String s = exact ? "" : "at most ";
		return "Expected " + s + Formatting.calls(maxCalls) + " but got " + Formatting.calls(actual);
	}

}
