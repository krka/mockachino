package se.mockachino.alias;

import se.mockachino.MethodCall;

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
	public List<MethodCall> getMatches() {
		SortedSet<MethodCall> set = new TreeSet<MethodCall>(MethodCall.COMPARATOR);
		for (Alias alias : aliases) {
			List<MethodCall> list = alias.getMatches();
			for (MethodCall methodCall : list) {
				set.add(methodCall);
			}
		}
		return new ArrayList<MethodCall>(set);
	}
}
