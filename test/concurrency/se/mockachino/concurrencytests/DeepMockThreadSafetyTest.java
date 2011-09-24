package se.mockachino.concurrencytests;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.Settings;
import se.mockachino.matchers.Matchers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeepMockThreadSafetyTest {

    private static final int ITERATIONS = 1000;
    private static final int THREADS = 10;

    @Test
    public void testDeepMock() throws Exception {
        final List<List<String>> mock = Mockachino.mock(new TypeToken<List<List<String>>>() {}, Settings.fallback(Mockachino.DEEP_MOCK));
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        final CountDownLatch latch = new CountDownLatch(THREADS);
        final CountDownLatch latch2 = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    latch2.countDown();
                    try {
                        latch2.await();
                        for (int i = 0; i < ITERATIONS; i++) {
                            mock.get(i).add("Foo");
                        }
                    } catch (InterruptedException e) {
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        while (!latch.await(1, TimeUnit.SECONDS)) {
            System.out.println(latch2.getCount() + ", " + latch.getCount());
        }
        for (int i = 0; i < ITERATIONS; i++) {
            Mockachino.verifyExactly(THREADS).on(mock.get(i)).add(Matchers.any(String.class));
        }
    }
}
