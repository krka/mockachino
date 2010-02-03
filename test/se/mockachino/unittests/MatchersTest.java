package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.matchers.Matchers;
import se.mockachino.matchers.matcher.ClassMatcher;
import se.mockachino.matchers.matcher.Matcher;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class MatchersTest {
	@Test
	public void testInt() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeInt(123);
		Mockachino.verifyOnce().on(mock).writeInt(123);
		Mockachino.verifyOnce().on(mock).writeInt(Matchers.anyInt());
	}

	@Test
	public void testLong() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeLong(123);
		Mockachino.verifyOnce().on(mock).writeLong(123);
		Mockachino.verifyOnce().on(mock).writeLong(Matchers.anyLong());
	}

	@Test
	public void testDouble() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeDouble(123.0);
		Mockachino.verifyOnce().on(mock).writeDouble(123.0);
		Mockachino.verifyOnce().on(mock).writeDouble(Matchers.anyDouble());
	}

	@Test
	public void testFloat() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeFloat(123.0f);
		Mockachino.verifyOnce().on(mock).writeFloat(123.0f);
		Mockachino.verifyOnce().on(mock).writeFloat(Matchers.anyFloat());
	}

	@Test
	public void testShort() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeShort(123);
		Mockachino.verifyOnce().on(mock).writeShort(123);
		Mockachino.verifyOnce().on(mock).writeShort(Matchers.anyShort());
	}

	@Test
	public void testByte() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeByte(123);
		Mockachino.verifyOnce().on(mock).writeByte(123);
		Mockachino.verifyOnce().on(mock).writeByte(Matchers.anyByte());
	}

	@Test
	public void testBoolean() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeBoolean(true);
		Mockachino.verifyOnce().on(mock).writeBoolean(true);
		Mockachino.verifyOnce().on(mock).writeBoolean(Matchers.anyBoolean());
	}

	@Test
	public void testComplex() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyOnce().on(mock).compare("Hello", "World");
		Mockachino.verifyNever().on(mock).compare("Hello", "Worldz");
		Mockachino.verifyNever().on(mock).compare("Helloz", "World");
		Mockachino.verifyExactly(2).on(mock).compare(Matchers.type(Object.class), Matchers.type(Object.class));
		Mockachino.verifyExactly(3).on(mock).compare(Matchers.any(Object.class), Matchers.any(Object.class));
		Mockachino.verifyOnce().on(mock).compare("Foo", null);
		Mockachino.verifyOnce().on(mock).compare("Foo", Matchers.type(Object.class));
		Mockachino.verifyExactly(2).on(mock).compare(Matchers.type(String.class), Matchers.type(String.class));
		Mockachino.verifyExactly(2).on(mock).compare(Matchers.type(String.class), Matchers.type(Object.class));
		Mockachino.verifyOnce().on(mock).compare(Matchers.type(String.class), Matchers.isNull());
		Mockachino.verifyOnce().on(mock).compare(Matchers.notNull(), Matchers.isNull());
		
	}

	@Test
	public void testNotMatcher() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyOnce().on(mock).compare(Matchers.not(ClassMatcher.create(List.class)), "World");
	}

	@Test
	public void testCustomMatcher() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Matcher<Object> myFooBarMatcher = new Matcher<Object>() {
			@Override
			public boolean matches(Object value) {
				return "Foo".equals(value) || "Bar".equals(value);
			}

			@Override
			public Class<Object> getType() {
				return Object.class;
			}

			@Override
			protected String asString() {
				return "(Foo or Bar)";
			}
		};
		Mockachino.verifyOnce().on(mock).compare(Matchers.matcher(myFooBarMatcher), Matchers.matcher(myFooBarMatcher));
	}
}
