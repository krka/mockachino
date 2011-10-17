package se.mockachino.alias;

import se.mockachino.Invocation;

import java.util.List;

public interface Alias {
	List<Invocation> getMatches();

	int count();


	Alias union(Alias other);

	Alias intersection(Alias other);

	void verifyOnce();

	void verifyNever();

	void verifyExactly(int n);

	void verifyAtMost(int max);

	void verifyAtLeast(int min);

	void verify(int min, int max);

}
