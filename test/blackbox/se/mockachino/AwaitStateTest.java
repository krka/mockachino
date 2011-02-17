package se.mockachino;

import org.junit.Test;
import se.mockachino.alias.Alias;
import se.mockachino.alias.SimpleAlias;
import se.mockachino.matchers.Matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static se.mockachino.matchers.Matchers.any;

public class AwaitStateTest {
    @Test
    public void testAwait() {
        final List<String> ref = new ArrayList<String>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ref.add("Foo");
            }
        }, 50);
        Mockachino.awaitState(100, 1, ref).size();
    }

    @Test
    public void testAwait2() {
        final List<String> ref = new ArrayList<String>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ref.add("Foo");
            }
        }, 50);
        Mockachino.awaitState(100, "Foo", ref).get(0);
    }
}
