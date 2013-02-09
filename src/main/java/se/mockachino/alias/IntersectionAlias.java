package se.mockachino.alias;

import se.mockachino.Invocation;

import java.util.*;

public class IntersectionAlias extends AbstractAlias {
	private final Alias[] aliases;

	public IntersectionAlias(Alias... aliases) {
		this.aliases = aliases;
	}

	@Override
	public List<Invocation<?>> getMatches() {
		int n = aliases.length;
		if (n == 0) {
			return Collections.emptyList();
		}
		SortedSet<Invocation<?>> set = new TreeSet<Invocation<?>>(Invocation.COMPARATOR);
		set.addAll(aliases[0].getMatches());
		for (int i = 1; i < n; i++) {
			Alias alias = aliases[i];
			List<Invocation<?>> list = alias.getMatches();
			set.retainAll(list);
		}
		return new ArrayList<Invocation<?>>(set);
	}
}
