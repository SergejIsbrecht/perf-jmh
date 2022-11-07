package io.sergejisbrecht;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

public class VolatileAtomicBench {
  @State(Scope.Benchmark)
  public static class VolatileState {
    volatile boolean isDisposed = false;
  }

  @State(Scope.Benchmark)
  public static class AtomicState {
    final AtomicBoolean isDisposed = new AtomicBoolean(false);
  }

  @State(Scope.Benchmark)
  public static class AtomicSwapState {
    final AtomicBoolean isDisposed = new AtomicBoolean(false);
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void volatileSetX(VolatileState state, Blackhole blackhole) throws Exception {
    state.isDisposed = true;
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void volatileReadX(VolatileState state, Blackhole blackhole) throws Exception {
    blackhole.consume(state.isDisposed);
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void volatileCASX(VolatileState state, Blackhole blackhole) throws Exception {
    blackhole.consume(state.isDisposed);
    state.isDisposed = true;
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void atomicSetX(AtomicState state) throws Exception {
    state.isDisposed.set(true);
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void atomicGetAndSetX(AtomicState state, Blackhole blackhole) throws Exception {
    blackhole.consume(state.isDisposed.getAndSet(true));
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void atomicReadX(AtomicState state, Blackhole blackhole) throws Exception {
    blackhole.consume(state.isDisposed.get());
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void atomicSwapX(AtomicSwapState state, Blackhole blackhole) throws Exception {
    blackhole.consume(state.isDisposed.compareAndSet(false, true));
  }
}
