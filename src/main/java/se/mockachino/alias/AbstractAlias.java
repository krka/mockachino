package se.mockachino.alias;

import se.mockachino.exceptions.VerificationError;

public abstract class AbstractAlias implements Alias {
	@Override
	public Alias union(Alias other) {
		return new UnionAlias(this, other);
	}

	@Override
	public Alias intersection(Alias other) {
		return new IntersectionAlias(this, other);
	}

	@Override
	public int count() {
		return getMatches().size();
	}

	@Override
	public void verify(int min, int max) {
		verify(min, max, "at least", "at most");
	}

	private void verify(int min, int max, String tooFew, String tooMany) {
		int count = count();
		if (count < min) {
			String s = "Expected %s %d calls but got %d";
			throw new VerificationError(String.format(s, tooFew, min, count));
		}
		if (count > max) {
			String s = "Expected %s %d calls but got %d";
			throw new VerificationError(String.format(s, tooMany, max, count));
		}
	}

	@Override
	public void verifyAtLeast(int min) {
		verify(min, Integer.MAX_VALUE, "at least", "at most");
	}

	@Override
	public void verifyAtMost(int max) {
		verify(0, max, "at least", "at most");
	}

	@Override
	public void verifyExactly(int n) {
		verify(n, n, "exactly", "exactly");
	}

	@Override
	public void verifyNever() {
		verifyExactly(0);
	}

	@Override
	public void verifyOnce() {
		verifyExactly(1);
	}
}
