package se.mockachino.alias;

import se.mockachino.Invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class DifferenceAlias extends AbstractAlias {
	private final Alias keep;
	private final Alias remove;

	public DifferenceAlias(Alias keep, Alias remove) {
		this.keep = keep;
		this.remove = remove;
	}

	@Override
	public List<Invocation> getMatches() {
		SortedSet<Invocation> set = new TreeSet<Invocation>(Invocation.COMPARATOR);
		set.addAll(keep.getMatches());
		set.removeAll(remove.getMatches());
		return new ArrayList<Invocation>(set);
	}
}
