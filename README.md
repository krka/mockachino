# mockachino
Mock framework for java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/se.mockachino/mockachino/badge.svg)](https://maven-badges.herokuapp.com/maven-central/se.mockachino/mockachino)

For ease of use and increased readability in your tests, consider adding these static imports 
```java
import static se.mockachino.Mockachino.*; 
import static se.mockachino.matchers.Matchers.*; 
import static se.mockachino.Settings.*;
```

Now it's time to start writing your tests! This guide starts with the fundamentals all the way to the most advanced usage (which isn't really that advanced). Mockachino makes mocking simple.
##Basic usage
Basic mock usage is easy
```java
@Test 
public void testExample() { 
    // Create a mock 
    List mock = mock(List.class);

    // Interact with it
    mock.get(123);
    mock.get(456);

    verifyOnce().on(mock).get(123);
    verifyOnce().on(mock).get(456);

    // Verify that some interactions never occured
    verifyNever().on(mock).get(124);
}
```
##Stubbing
Sometimes you need to setup your mocks to return values for the tested code to work. This is called stubbing.
```java
@Test 
public void testExampleStub() { 
    // Create a mock 
    List mock = mock(List.class);

    // Stub a return value
    stubReturn("Hello").on(mock).get(123);

    // Interact with it
    Object hello = mock.get(123);
    Object shouldBeNull = mock.get(456);
    assertEquals("Hello", hello);
    assertEquals(null, shouldBeNull);

    // Verify calls
    verifyOnce().on(mock).get(123);
    verifyOnce().on(mock).get(456);
}

```
##Using matchers
A matcher is something you can use instead of regular arguments when verifying or stubbing. A matcher makes it possible to verify interactions with parameters from a range of values.Here's a simple example
```java
@Test 
public void testExampleWithMatchers() { 
    // Create a mock 
    List mock = mock(List.class);

    mock.add("Hello");
    mock.add("World");

    // Verify calls
    verifyOnce().on(mock).add(eq("Hello"));
    verifyExactly(2).on(mock).add(any(String.class));
}
```
`eq()` is the most basic matcher, in fact all plain arguments get converted to an eq-matcher when verifying or stubbing. The `any()` matcher is useful if you want to accept any object. The class parameter is just there to make the compiler happy.


##Multiple stubbers
The last defined stub will take precedence if multiple stubbers match, so you can set up default values-
```java
@Test 
public void testExampleStubOverride() { 
    // Create a mock 
    List mock = mock(List.class);

    // Stub a return value
    stubReturn("NullString").on(mock).get(anyInt());
    stubReturn("Hello").on(mock).get(123);

    // Interact with it
    assertEquals("Hello", mock.get(123));
    assertEquals("NullString", mock.get(1));
    assertEquals("NullString", mock.get(2));

    // Verify calls
    verifyExactly(3).on(mock).get(anyInt());
}
```
##Mixing matchers and plain arguments
It's possible to mix matchers and plain arguments in the same method verification or stubbing in most cases. The only exception is when the plain arguments is `null`, `0`, `false` or `'\0'`. Here is how not to use it
```java
@Test 
public void testBadMatcherCombo() { 
    // Create a mock 
    List mock = mock(List.class);

    // This works
    verifyNever().on(mock).add(eq(0), any(Object.class));

    // This doesn't
    try {
        verifyNever().on(mock).add(0, any(Object.class));
        fail("This should never pass");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

```
The output: 
>se.mockachino.exceptions.VerificationError: Illegal mix of matchers and default values in the same method verification or stubbing. Replace null, 0, '\0' and false with matchers. at se.mockachino.unittests.ExamplesTest.testBadMatcherCombo(ExamplesTest.java:90)

##Useful error messages
If a verification fails, you'll get an informative output text.
```java
@Test 
    public void testSimpleVerify() { 
    try { 
        List mock = mock(List.class); 
        mock.get(1); 
        mock.get(2); 
        mock.get(3);

        verifyAtLeast(2).on(mock).get(3);
        fail("This should never pass");
    } catch (VerificationError e) {
        e.printStackTrace();
    }
}
```
The output: 
>se.mockachino.exceptions.VerificationError: Expected at least 2 calls, but got 1 call ACTUAL: mock.get(1) ACTUAL: mock.get(2) ACTUAL: (HIT) mock.get(3) EXPECTED: mock.get(3) at se.mockachino.unittests.ExamplesTest.testSimpleVerify(ExamplesTest.java:106)

##Verification in order
It's easy to verify that calls arrive in a certain order, just create a new order object like this.
```java
@Test 
public void testInOrder() { 
    List mock = mock(List.class); 
    mock.add("Hello"); 
    mock.remove("Hello"); 
    mock.add("World"); 
    mock.contains("Hello"); 
    mock.add("Bar");

    OrderingContext order = newOrdering();
    order.verify().on(mock).add("Hello");
    order.verify().on(mock).add("World");
    order.verify().on(mock).contains("Hello");
    order.verify().on(mock).add("Bar");
}
```
##Using spies
Spying is almost the same as mocking, the difference is that all method calls by default go through the spied on object. You can still stub away calls to objects if you want, so the spied object can be thought of as just the default stub for a mock. You spy on an object by providing both the interface (or class) that's relevant and then the actual object to spy on.
```java
@Test 
public void testSpy() { 
    // Setup spied object 
    ArrayList myList = new ArrayList(); 
    myList.add("Real object");

    // Create spy on object
    List spy = spy(myList);

    // Show that calls to the spy go to the real object
    assertEquals(1, spy.size());
    assertEquals("Real object", spy.get(0));

    // Overwrite spy.get(0)
    stubReturn("Fake object").on(spy).get(0);
    spy.add("Real object 2");

    // Show that the overwrite worked,
    // but other calls still go through the real object
    assertEquals(2, spy.size());
    assertEquals("Fake object", spy.get(0));
    assertEquals("Real object 2", spy.get(1));

    // Verifying the calls still work of course
    verifyExactly(2).on(spy).size();
    verifyExactly(3).on(spy).get(anyInt());
    verifyExactly(1).on(spy).add(any(Object.class));
}

```
##Combining matchers
Some matchers combines other matchers. The simplest example is `Matchers.not`, which inverts a matcher. The matcher below matches `compare("Hello", "World")` since `mType(List.class)` does not match `"Hello"` since it's not a list.
```java
@Test 
public void testNotMatcher() { 
    Comparator mock = mock(Comparator.class);
    compare("Hello", "World"); 
    compare("Foo", "Bar"); 
    compare("Foo", null);

    verifyOnce().on(mock).compare(not(mType(List.class)), "World");
}
```
##Catching values with matchers
Sometimes you want to catch the value that matches a specific matcher. That's really easy.
```java
@Test 
public void testSimple() { 
    List mock = mock(List.class);
    ArgumentCatcher<Integer> catcher = ArgumentCatcher.create(mAnyInt());
    mock.get(123);
    verifyOnce().on(mock).get(match(catcher));
    assertEquals(Integer.valueOf(123), catcher.getValue()); 
}
```
##Active observing on method calls
It's easy to add your own callback for mock method calls, for whichever reason. You can use matchers for designing what arguments to listen to, just as with stubbing and verifying. The example below shows how it's done.
```java
@Test 
public void testSimple() { 
    List mock = Mockachino.mock(List.class);

    final List<String> list = new ArrayList<String>();
    CallHandler listener = new CallHandler() {
        @Override
        public Object invoke(Object obj, MethodCall call) {
            list.add(call.toString());
            return null;
        }
    };
    observeWith(listener).on(mock).get(123);

    mock.get(123);
    mock.get(124);
    assertEquals(1, list.size());
    assertEquals("get(123)", list.get(0));
}
```
##Stubbing with throws
You can of course stub a method to throw an exception instead of returning a value.
```java
@Test 
public void testException() { 
    try { 
        List mock = mock(List.class);
        stubThrow(new ArrayIndexOutOfBoundsException("Hello")).on(mock).add(type(Object.class)); 
        mock.add(""); 
        fail("Should have thrown an exception"); 
    } catch (Exception e) { 
        assertEquals("Hello", e.getMessage()); 
    }
}
```
##More advanced stubbing with answers
If you need complex behavior, you can implement your own answering strategy and plug into the mock.
```java
@Test 
public void testAnswer() { 
    List mock = mock(List.class); 
    stubAnswer(new CallHandler() { 
        @Override public Object invoke(Object obj, MethodCall call) { 
            Object first = call.getArguments()[0]; 
            return first.toString() + first.toString(); 
        }
    }).on(mock).get(anyInt());

    for (int i = 0; i < 100; i++) {
        String s = "" + i;
        String s2 = s + s;
        assertEquals(s2, mock.get(i));
    }
    OrderingContext order = newOrdering();
    for (int i = 0; i < 100; i++) {
        order.verify().on(mock).get(i);
    }

    verifyExactly(100).on(mock).get(anyInt());

}

```
