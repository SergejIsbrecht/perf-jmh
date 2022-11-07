package io.sergejisbrecht;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HelloWorld {
  public static void main(String[] args) throws Throwable {
    Options opt = new OptionsBuilder()
        .include(VolatileAtomicBench.class.getSimpleName())
        .forks(0)
        .warmupIterations(5)
        .measurementIterations(10)
        .build();
    new Runner(opt).run();
  }
}
