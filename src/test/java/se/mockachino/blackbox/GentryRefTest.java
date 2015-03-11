package se.mockachino.blackbox;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.when;

public class GentryRefTest {
	public static final TypeToken<Attribute<A>> ATTRIBUTE_TYPE_TOKEN = new TypeToken<Attribute<A>>() {
	};

	@Test
	public void exposeBug() throws Exception {
		AttributeKey<A> key = new AttributeKey<A>();
		Attribute<A> attribute = mock(ATTRIBUTE_TYPE_TOKEN);
		AttributeMap attributeMap = mock(AttributeMap.class);

		when(attributeMap.getAttribute(key)).thenReturn(attribute);

		Attribute<A> returnedAttribute = attributeMap.getAttribute(key);
		assertSame(attribute, returnedAttribute);
	}


	public interface AttributeMap {
		<T> Attribute<T> getAttribute(AttributeKey<T> key);
	}

	public interface Attribute<T> {

	}

	public static final class AttributeKey<T> {

	}

	public static class A {

	}
}
