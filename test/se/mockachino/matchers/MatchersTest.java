package se.mockachino.matchers;

import org.junit.Test;
import se.mockachino.Mockachino;
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
		Mockachino.verify(mock).writeInt(123);
		Mockachino.verify(mock).writeInt(Matchers.anyInt());
	}

	@Test
	public void testLong() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeLong(123);
		Mockachino.verify(mock).writeLong(123);
		Mockachino.verify(mock).writeLong(Matchers.anyLong());
	}

	@Test
	public void testDouble() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeDouble(123.0);
		Mockachino.verify(mock).writeDouble(123.0);
		Mockachino.verify(mock).writeDouble(Matchers.anyDouble());
	}

	@Test
	public void testFloat() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeFloat(123.0f);
		Mockachino.verify(mock).writeFloat(123.0f);
		Mockachino.verify(mock).writeFloat(Matchers.anyFloat());
	}

	@Test
	public void testShort() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeShort(123);
		Mockachino.verify(mock).writeShort(123);
		Mockachino.verify(mock).writeShort(Matchers.anyShort());
	}

	@Test
	public void testByte() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeByte(123);
		Mockachino.verify(mock).writeByte(123);
		Mockachino.verify(mock).writeByte(Matchers.anyByte());
	}

	@Test
	public void testBoolean() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeBoolean(true);
		Mockachino.verify(mock).writeBoolean(true);
		Mockachino.verify(mock).writeBoolean(Matchers.anyBoolean());
	}

	@Test
	public void testComplex() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyExactly(mock, 1).compare("Hello", "World");
		Mockachino.verifyExactly(mock, 0).compare("Hello", "Worldz");
		Mockachino.verifyExactly(mock, 0).compare("Helloz", "World");
		Mockachino.verifyExactly(mock, 2).compare(Matchers.type(Object.class), Matchers.type(Object.class));
		Mockachino.verifyExactly(mock, 3).compare(Matchers.any(Object.class), Matchers.any(Object.class));
		Mockachino.verifyExactly(mock, 1).compare("Foo", null);
		Mockachino.verifyExactly(mock, 1).compare("Foo", Matchers.type(Object.class));
		Mockachino.verifyExactly(mock, 2).compare(Matchers.type(String.class), Matchers.type(String.class));
		Mockachino.verifyExactly(mock, 2).compare(Matchers.type(String.class), Matchers.type(Object.class));
		Mockachino.verifyExactly(mock, 1).compare(Matchers.type(String.class), Matchers.isNull());
		Mockachino.verifyExactly(mock, 1).compare(Matchers.notNull(), Matchers.isNull());
		
	}

	@Test
	public void testNotMatcher() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyExactly(mock, 1).compare(Matchers.not(ClassMatcher.create(List.class)), "World");
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
		Mockachino.verifyExactly(mock, 1).compare(Matchers.matcher(myFooBarMatcher), Matchers.matcher(myFooBarMatcher));
	}
}
