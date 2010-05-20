package se.mockachino.verifiers;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AndVerifier extends AbstractVerifier {
	private final Verifier[] verifiers;

	public AndVerifier(Verifier... verifiers) {
		this.verifiers = verifiers;
	}

	@Override
	public List<MethodCall> getMatches() {
		int n = verifiers.length;
		if (n == 0) {
			return Collections.emptyList();
		}
		SortedSet<MethodCall> set = new TreeSet<MethodCall>(MethodCall.COMPARATOR);
		set.addAll(verifiers[0].getMatches());
		for (int i = 1; i < n; i++) {
			Verifier verifier = verifiers[i];
			List<MethodCall> list = verifier.getMatches();
			set.retainAll(list);
		}
		return new ArrayList<MethodCall>(set);
	}
}
