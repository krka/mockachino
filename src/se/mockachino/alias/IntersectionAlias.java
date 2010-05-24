package se.mockachino.alias;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntersectionAlias extends AbstractAlias {
	private final Alias[] aliases;

	public IntersectionAlias(Alias... aliases) {
		this.aliases = aliases;
	}

	@Override
	public List<MethodCall> getMatches() {
		int n = aliases.length;
		if (n == 0) {
			return Collections.emptyList();
		}
		SortedSet<MethodCall> set = new TreeSet<MethodCall>(MethodCall.COMPARATOR);
		set.addAll(aliases[0].getMatches());
		for (int i = 1; i < n; i++) {
			Alias alias = aliases[i];
			List<MethodCall> list = alias.getMatches();
			set.retainAll(list);
		}
		return new ArrayList<MethodCall>(set);
	}
}
