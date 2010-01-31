package se.mockachino.matchers;

public class AnyIntMatcher extends ClassMatcher {
	public AnyIntMatcher() {
		super(int.class, Integer.class);
	}
}
