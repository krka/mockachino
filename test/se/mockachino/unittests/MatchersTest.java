package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.matchers.matcher.Matcher;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.matchers.Matchers.*;

public class MatchersTest {
	@Test
	public void testInt() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeInt(123);
		Mockachino.verifyOnce().on(mock).writeInt(123);
		Mockachino.verifyOnce().on(mock).writeInt(mAnyInt());
	}

	@Test
	public void testLong() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeLong(123);
		Mockachino.verifyOnce().on(mock).writeLong(123);
		Mockachino.verifyOnce().on(mock).writeLong(mAnyLong());
	}

	@Test
	public void testDouble() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeDouble(123.0);
		Mockachino.verifyOnce().on(mock).writeDouble(123.0);
		Mockachino.verifyOnce().on(mock).writeDouble(mAnyDouble());
	}

	@Test
	public void testFloat() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeFloat(123.0f);
		Mockachino.verifyOnce().on(mock).writeFloat(123.0f);
		Mockachino.verifyOnce().on(mock).writeFloat(mAnyFloat());
	}

	@Test
	public void testShort() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeShort(123);
		Mockachino.verifyOnce().on(mock).writeShort(123);
		Mockachino.verifyOnce().on(mock).writeShort(mAnyShort());
	}

	@Test
	public void testByte() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeByte(123);
		Mockachino.verifyOnce().on(mock).writeByte(123);
		Mockachino.verifyOnce().on(mock).writeByte(mAnyByte());
	}

	@Test
	public void testBoolean() throws IOException {
		DataOutput mock = Mockachino.mock(DataOutput.class);
		mock.writeBoolean(true);
		Mockachino.verifyOnce().on(mock).writeBoolean(true);
		Mockachino.verifyOnce().on(mock).writeBoolean(mAnyBoolean());
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
		Mockachino.verifyExactly(2).on(mock).compare(mType(Object.class), mType(Object.class));
		Mockachino.verifyExactly(3).on(mock).compare(mAny(Object.class), mAny(Object.class));
		Mockachino.verifyOnce().on(mock).compare("Foo", null);
		Mockachino.verifyOnce().on(mock).compare("Foo", mType(Object.class));
		Mockachino.verifyExactly(2).on(mock).compare(mType(String.class), mType(String.class));
		Mockachino.verifyExactly(2).on(mock).compare(mType(String.class), mType(Object.class));
		Mockachino.verifyOnce().on(mock).compare(mType(String.class), mNull());
		Mockachino.verifyOnce().on(mock).compare(mNotNull(), mNull());
		
	}

	@Test
	public void testAnd() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyExactly(2).on(mock).compare(
				mAnd(
						eq("Foo", Object.class), type(Object.class)),
				mOr(eq("Bar", Object.class), isNull()));
	}

	@Test
	public void testCustom() {
		List mock = Mockachino.mock(List.class);
		Matcher<Integer> myMatcher = new Matcher<Integer>() {
			@Override
			public boolean matches(Integer value) {
				return true;
			}

			@Override
			public Class<Integer> getType() {
				return Integer.class;
			}
		};

		Mockachino.stubReturn("Foo").on(mock).get(match(myMatcher));
		assertEquals("Foo", mock.get(1));
	}
}
