package se.mockachino.alias;

import se.mockachino.Invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class UnionAlias extends AbstractAlias {
	private final Alias[] aliases;

	public UnionAlias(Alias... aliases) {
		this.aliases = aliases;
	}

	@Override
	public List<Invocation<?>> getMatches() {
		SortedSet<Invocation<?>> set = new TreeSet<Invocation<?>>(Invocation.COMPARATOR);
		for (Alias alias : aliases) {
			List<Invocation<?>> list = alias.getMatches();
			for (Invocation invocation : list) {
				set.add(invocation);
			}
		}
		return new ArrayList<Invocation<?>>(set);
	}
}
