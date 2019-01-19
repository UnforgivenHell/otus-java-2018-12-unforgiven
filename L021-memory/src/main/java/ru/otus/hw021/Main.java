package ru.otus.hw021;

import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) {
        int size = 1_000_000;
        int timeWait = 10;

        try {
            Benchmark benchmark = new Benchmark(size, timeWait);

            benchmark.run(()-> new String(new char[0]));
            benchmark.run(()-> new String(new byte[0]));
            benchmark.run(()-> new String());

            benchmark.run(()->new Integer("0"));

            benchmark.run(()->new Short("0"));

            benchmark.run(()->new Byte("0"));

            benchmark.run(()->new Long("0"));

            benchmark.run(()->new Float("0"));

            benchmark.run(()-> new MyClass());
        } catch (OutOfMemoryError e) {
            System.out.println("Not Enough memory");
        } catch (InterruptedException e) {
            System.out.printf("Interrupted exception happened, message: %s\n", e.getMessage());
        }
    }
}
