package se.mockachino.verifiers;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class OrVerifier extends AbstractVerifier {
	private final Verifier[] verifiers;

	public OrVerifier(Verifier... verifiers) {
		this.verifiers = verifiers;
	}

	@Override
	public List<MethodCall> getMatches() {
		SortedSet<MethodCall> set = new TreeSet<MethodCall>(MethodCall.COMPARATOR);
		for (Verifier verifier : verifiers) {
			List<MethodCall> list = verifier.getMatches();
			for (MethodCall methodCall : list) {
				set.add(methodCall);
			}
		}
		return new ArrayList<MethodCall>(set);
	}
}
