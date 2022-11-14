package io.sergejisbrecht;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

public class VolatileAtomicBench {
  @Benchmark
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void volatileSetX(VolatileState state, Blackhole blackhole) throws Exception {
    IntStream.range(0, 10_000)
        .forEach(value -> {
          state.executorService.submit(() -> {
            synchronized (state.lock) {
              Blackhole.consumeCPU(1_000);
            }
          });
        });

    CountDownLatch countDownLatch = new CountDownLatch(1);
    state.executorService.submit(() -> countDownLatch.countDown());

    boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
    blackhole.consume(await);
  }

  @State(Scope.Benchmark)
  public static class VolatileState {
    private final Object lock = new Object();
    private ExecutorService executorService;

    @Setup
    public void prepare() {
      executorService = Executors.newFixedThreadPool(6);
    }

    @TearDown
    public void check() {
      executorService.shutdown();
    }
  }
}
