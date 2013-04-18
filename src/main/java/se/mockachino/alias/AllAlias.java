package se.mockachino.alias;

import se.mockachino.Invocation;
import se.mockachino.Mockachino;

import java.util.ArrayList;
import java.util.List;

public class AllAlias extends se.mockachino.alias.AbstractAlias {
	private final Iterable<Invocation> invocations;

	public AllAlias(Iterable<Invocation> invocations) {
		this.invocations = invocations;
	}

	public static Alias fromMock(Object mock) {
		return new AllAlias(Mockachino.getData(mock).getInvocations());
	}

	@Override
	public List<Invocation> getMatches() {
		ArrayList<Invocation> res = new ArrayList<Invocation>();
		for (Invocation invocation : invocations) {
			res.add(invocation);
		}
		return res;
	}
}
