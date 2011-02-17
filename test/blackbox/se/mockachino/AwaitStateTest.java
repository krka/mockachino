package se.mockachino;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        Mockachino.awaitState(300, 1, ref).size();
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
        Mockachino.awaitState(300, "Foo", ref).get(0);
    }
}
